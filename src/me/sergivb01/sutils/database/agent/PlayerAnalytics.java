package me.sergivb01.sutils.database.agent;

import com.instrumentalapp.Agent;
import me.sergivb01.sutils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerAnalytics implements Listener{
	private static Agent agent;

	public PlayerAnalytics(ServerUtils instance){
		Bukkit.getPluginManager().registerEvents(this, instance);
	}

	@EventHandler
	public void onPlayerJoin(){
		//TODO: Something
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		//TODO: Something
	}

	static {
		agent = AgentManager.getAgent();
	}

}
