package me.sergivb01.sutils.player;

import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.player.data.PlayerProfile;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.ChatColor.*;

public class PlayerListener implements Listener{
	private ServerUtils instance;

	public PlayerListener(ServerUtils instance){
		this.instance = instance;
		Bukkit.getPluginManager().registerEvents(this, instance);
		for(Player player : Bukkit.getOnlinePlayers()){
			player.kickPlayer("Relog please.");
		}
	}

	@EventHandler
	public void onPreLogin(PlayerPreLoginEvent event){
		if(MongoDBDatabase.isBlacklisted(event.getUniqueId())){
			event.setKickMessage(RED + "Your access to "+ DARK_RED +"MyServer Network" + RED + " has been blocked. \n\n"
					+ "You may appeal this blacklist @  ts.myserver.net"
			);
			event.setResult(PlayerPreLoginEvent.Result.KICK_BANNED);
		}else if(MongoDBDatabase.getLastBan(event.getUniqueId()) != null){
			event.setKickMessage(translateAlternateColorCodes('&', MongoDBDatabase.getLastBan(event.getUniqueId()).getString("reason")));
			event.setResult(PlayerPreLoginEvent.Result.KICK_BANNED);
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

		playerProfile.getPlayerData().setBalance(500);
		player.sendMessage(Cache.getPlayerProfile(player).getPlayerData().getBalance() + "");

		Cache.getPlayerProfile(player.getUniqueId()).save(true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Cache.getPlayerProfile(player.getUniqueId()).save(true);
		Cache.removeProfile(player);
	}


}
