package me.sergivb01.sutils.queue;

import me.sergivb01.sutils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class QueueListener implements Listener{

	public QueueListener(ServerUtils instance){
		Bukkit.getPluginManager().registerEvents(this, instance);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance,() -> {
        	    QueueAPI.statuses.forEach((s, document) -> {
        	    	//System.out.println("Status for " + s + ":");
        	    	//System.out.println(document.toJson());
	            });
        },20L, 5 * 20L);
	}


}
