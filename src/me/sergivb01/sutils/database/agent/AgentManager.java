package me.sergivb01.sutils.database.agent;

import com.instrumentalapp.Agent;
import com.instrumentalapp.AgentOptions;
import lombok.Getter;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bukkit.Bukkit;

public class AgentManager {
	private ServerUtils instance;
	@Getter public static Agent agent;
	private static String prefix = "mcserver.";

	public AgentManager(ServerUtils instance){
		this.instance = instance;
		init();
	}

	private void init(){
		agent = new Agent(new AgentOptions().setApiKey(ConfigUtils.AGENT_API_KEY).setEnabled(true).setSynchronous(true));
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, ()->{
			if(!agent.isRunning()){ //Avoid possible crashes
				return;
			}
			Runtime runtime = Runtime.getRuntime();
			agent.gauge(prefix + ConfigUtils.SERVER_NAME + ".tps", Bukkit.spigot().getTPS()[0]);
			agent.gauge(prefix + ConfigUtils.SERVER_NAME + ".online", Bukkit.getOnlinePlayers().size());
			agent.gauge(prefix + ConfigUtils.SERVER_NAME + ".freememory", runtime.freeMemory() / 1024);
			agent.gauge(prefix + ConfigUtils.SERVER_NAME + ".maxmemory", runtime.maxMemory() / 1024);
		}, 20L, 10 * 20L);
	}


}
