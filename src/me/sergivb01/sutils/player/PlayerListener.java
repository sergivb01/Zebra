package me.sergivb01.sutils.player;

import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.payload.PayloadSender;
import me.sergivb01.sutils.utils.ConfigUtils;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import net.veilmc.base.BasePlugin;
import net.veilmc.hcf.utils.ConfigurationService;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static me.sergivb01.sutils.database.mongo.MongoDBDatabase.*;
import static me.sergivb01.sutils.utils.InventoryUtils.*;
import static org.bukkit.ChatColor.*;

public class PlayerListener implements Listener{
	private ServerUtils instance;

	public PlayerListener(ServerUtils instance){
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
		for(Player player : Bukkit.getOnlinePlayers()){
			Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(player, "placeholder"));
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();

		new FancyMessage("Your profile is available at ")
				.color(YELLOW)
				.then("https://veilhcf.us/u/" + player.getName())
				.color(GREEN)
				.link("https://veilhcf.us/u/" + player.getName())
				.tooltip("Click to open your profile")
				.send(player);

		doAsyncLater(() -> {
			MongoDBDatabase.saveProfileToDatabase(player, true);
			player.sendMessage(YELLOW + "Your profile has been saved.");

			if(player.hasPermission("rank.staff"))
				PayloadSender.sendSwitch(player.getName(), "joined");
		}, 20L);

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		MongoDBDatabase.saveProfileToDatabase(player, false);
		if(player.hasPermission("rank.staff")){
			PayloadSender.sendSwitch(player.getName(), "left");
		}
	}

	@EventHandler
	public void onStaffChatChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(!player.hasPermission("rank.staff")){
			return;
		}
		if(BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isInStaffChat()){//Staffchat
			PayloadSender.sendStaffchat(player.getName(), event.getMessage().replace(";", ":"));
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent event){
		if(ConfigurationService.KIT_MAP){
			return;
		}
		UUID playerUUID = event.getEntity().getUniqueId();
		UUID killerUUID = (event.getEntity().getKiller() != null) ? event.getEntity().getKiller().getUniqueId() : null;
		String deathMSG = event.getDeathMessage();

		Location location = event.getEntity().getLocation();

		Document document = new Document("death_id", UUID.randomUUID())
				.append("dead", playerUUID)
				.append("dead_str", playerUUID.toString())
				.append("killer", (killerUUID != null) ? killerUUID : "ENVIRONMENT")
				.append("killer_str", (killerUUID != null) ? killerUUID.toString() : "ENVIRONMENT")
				.append("deathmsg", deathMSG)
				.append("location", location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ())
				.append("content-death", getInventoryAsJSON(event.getEntity()))
				.append("content-killer", (killerUUID != null) ? getInventoryAsJSON(event.getEntity().getKiller()) : "none")
				.append("server", ConfigUtils.SERVER_NAME)
				.append("timestamp", System.currentTimeMillis());

		addDeathSave(document);
	}

	private void doAsyncLater(Runnable runnable, long delay){
		Bukkit.getScheduler().runTaskLaterAsynchronously(instance, runnable, delay);
	}

}
