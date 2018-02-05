package me.sergivb01.sutils.database.agent;

import com.instrumentalapp.Agent;
import com.instrumentalapp.AgentOptions;
import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bukkit.Bukkit;

import java.text.NumberFormat;

public class AgentManager {
	private ServerUtils instance;
	@Getter public static Agent agent;

	public AgentManager(ServerUtils instance){
		this.instance = instance;
		init();
	}

	private void init(){
		agent = new Agent(new AgentOptions().setApiKey(ConfigUtils.AGENT_API_KEY).setEnabled(true));
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, ()->{
			agent.gauge(ConfigUtils.SERVER_NAME + ".tps", Bukkit.spigot().getTPS()[0]);
			agent.gauge(ConfigUtils.SERVER_NAME + ".online", Bukkit.getOnlinePlayers().size());
			agent.gauge(ConfigUtils.SERVER_NAME + ".freememory", runtime.freeMemory() / 1024);
			agent.gauge(ConfigUtils.SERVER_NAME + ".maxmemory", runtime.maxMemory() / 1024);
		}, 20L, 5 * 20L);
	}


}
