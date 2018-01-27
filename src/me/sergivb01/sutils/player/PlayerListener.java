package me.sergivb01.sutils.player;

import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.player.data.PlayerProfile;
import me.sergivb01.sutils.utils.ConfigUtils;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import net.veilmc.base.BasePlugin;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.sergivb01.sutils.database.mongo.MongoDBDatabase.addPlayerDeath;
import static me.sergivb01.sutils.database.mongo.MongoDBDatabase.getInventoryAsJSON;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.YELLOW;

public class PlayerListener implements Listener{
	private ServerUtils instance;

	public PlayerListener(ServerUtils instance){
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
		for(Player player : Bukkit.getOnlinePlayers()){
			player.performCommand("hub");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		PlayerProfile playerProfile = new PlayerProfile(player);
		Cache.addProfile(player.getUniqueId(), playerProfile);

		new FancyMessage("Your profile is available at ")
				.color(YELLOW)
				.then("https://example.com/u/" + player.getName())
				.color(GREEN)
				.link("https://example.com/u/" + player.getName())
				.tooltip("Click to open your profile")
				.send(player);

		Bukkit.getScheduler().runTaskLaterAsynchronously(instance, ()->{
			Cache.getPlayerProfile(player.getUniqueId()).save(true);
			player.sendMessage(YELLOW + "Your profile has been saved.");
		}, 20L);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(instance, ()->{
			Cache.getPlayerProfile(player.getUniqueId()).save(false);
			Cache.removeProfile(player);
		});
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

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		if(event.getEntity().getKiller() != null && event.getEntity() != null){
			Document document = new Document("dead", event.getEntity().getUniqueId())
					.append("killer", event.getEntity().getKiller().getUniqueId())
					.append("deathmsg", event.getDeathMessage())
					.append("location", event.getEntity().getLocation().toString())
					.append("content-death", getInventoryAsJSON(event.getEntity()))
					.append("content-killer", getInventoryAsJSON(event.getEntity().getKiller()))
					.append("timestamp", System.currentTimeMillis());

			addPlayerDeath(event.getEntity().getUniqueId(), document);
			addPlayerDeath(event.getEntity().getKiller().getUniqueId(), document);
			return;
		}

		if(event.getEntity() != null){
			Document document = new Document("dead", event.getEntity().getUniqueId())
					.append("killer", "ENVIROMENT")
					.append("deathmsg", event.getDeathMessage())
					.append("location", event.getEntity().getLocation().toString())
					.append("content-death", getInventoryAsJSON(event.getEntity()))
					.append("content-killer", "none")
					.append("timestamp", System.currentTimeMillis());
			addPlayerDeath(event.getEntity().getUniqueId(), document);
		}
	}

}
