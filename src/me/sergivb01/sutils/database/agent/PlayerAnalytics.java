package me.sergivb01.sutils.database.agent;

import com.instrumentalapp.Agent;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.utils.ConfigUtils;
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
		agent.increment(ConfigUtils.SERVER_NAME + ".online", +1, System.currentTimeMillis());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		agent.increment(ConfigUtils.SERVER_NAME + ".online", -1, System.currentTimeMillis());
	}

	static {
		agent = AgentManager.getAgent();
	}

}
