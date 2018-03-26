package me.sergivb01.sutils.player;

import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.payload.PayloadSender;
import me.sergivb01.sutils.utils.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class StaffListener implements Listener{
	private ServerUtils instance;
	private LogManager logManager;
	private String[] commands = {
			"more",
			"time",
			"sudo",
			"timer",
			"/set",
			"/stack",
			"crate",
			"lives",
			"tokens",
			"pex",
			"top",
			"enchant",
			"death"
			//TODO: More to add
	};

	public StaffListener(ServerUtils instance){
		this.instance = instance;
		this.logManager = new LogManager(instance);
		Bukkit.getPluginManager().registerEvents(this, instance);
	}

	@EventHandler
	public void onWorldEditCommand(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		if(!player.hasPermission("rank.staff") || player.getName().equalsIgnoreCase("sergivb01")){
			return;
		}

		String command = event.getMessage();
		for(String str : commands){
			if(command.toLowerCase().startsWith("/" + str)){
				logManager.formatMessage(player.getName(), "Executed command " + command);
				PayloadSender.sendStaffImportant(player.getName(), command);
			}
		}

	}


}
