package me.sergivb01.sutils.queue;

import me.sergivb01.sutils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QueueListener implements Listener{

	public QueueListener(ServerUtils instance){
		Bukkit.getPluginManager().registerEvents(this, instance);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance,() -> {
        	Bukkit.getOnlinePlayers()
              .stream()
              .filter(player -> QueueAPI.getPlayerQueue(player.getName()) != null)
              .forEach(
                  player -> {
                  	player.sendMessage("queue message todo");
                  	//TODO: Everything
                    /*Document playerDoc = QueueAPI.statuses.get(player.getName());
                    Document queueDoc = QueueAPI.queues.get(playerDoc.getString("server"));
					player.sendMessage(BLUE + "You are currently queud for " + WHITE + playerDoc.getString("server") + BLUE + ": " + (queueDoc.getBoolean("paused") ? RED  + "[PAUSED]" : ""));
	                player.sendMessage(BLUE + "Position " + WHITE + (playerDoc.getInteger("position") + 1) + BLUE + " of " + WHITE + queueDoc.getInteger("size"));
	                QueueAPI.statusOf(player.getName());*/
                  });
        },
        20L, 5 * 20L);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		QueueAPI.removePlayer(event.getPlayer().getName());
		if(QueueAPI.statuses.containsKey(event.getPlayer().getName())){
			QueueAPI.statuses.remove(event.getPlayer().getName());

		}
	}

}
