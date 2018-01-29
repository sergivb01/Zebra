package me.sergivb01.sutils.player;

import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import net.veilmc.base.BasePlugin;
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

import static me.sergivb01.sutils.database.mongo.MongoDBDatabase.addDeathSave;
import static me.sergivb01.sutils.utils.InventoryUtils.getInventoryAsJSON;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.YELLOW;

public class PlayerListener implements Listener{
	private static ServerUtils instance;

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
				.then("https://example.com/u/" + player.getName())
				.color(GREEN)
				.link("https://example.com/u/" + player.getName())
				.tooltip("Click to open your profile")
				.send(player);

		doAsyncLater(()->{
			MongoDBDatabase.saveProfileToDatabase(player, true);
			player.sendMessage(YELLOW + "Your profile has been saved.");
		}, 20L);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		MongoDBDatabase.saveProfileToDatabase(player, false);
		//TODO: Server switch
	}

	@EventHandler
	public void onStaffChatChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(!player.hasPermission("rank.staff")){
			return;
		}
		if(BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isInStaffChat()){//Staffchat
			RedisDatabase.getPublisher().write("staffchat;" + player.getName() + ";" + ConfigUtils.SERVER_NAME + ";" + event.getMessage().replace(";", ":"));
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled=true, priority= EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent event){
		UUID playerUUID = event.getEntity().getUniqueId();
		UUID killerUUID = (event.getEntity().getKiller() != null) ? event.getEntity().getKiller().getUniqueId() : null;
		String deathMSG = event.getDeathMessage();

		Location location = event.getEntity().getLocation();

		UUID deathID = UUID.randomUUID();
		Document document = new Document("death-id", deathID)
				.append("dead", playerUUID)
				.append("killer", (killerUUID != null) ? killerUUID : "ENVIRONMENT")
				.append("deathmsg", deathMSG)
				.append("location", location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ())
				.append("content-death", getInventoryAsJSON(event.getEntity())) //TODO: Is always air :(
				.append("content-killer", (killerUUID != null) ? getInventoryAsJSON(event.getEntity().getKiller()) : "none")
				.append("timestamp", System.currentTimeMillis());

		addDeathSave(document);
	}

	private static void doAsyncLater (Runnable runnable, long delay){
		Bukkit.getScheduler().runTaskLaterAsynchronously(instance, runnable, delay);
	}

}
