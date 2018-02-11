package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.queue.QueueAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

public class Queues implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] strings) {
	    QueueAPI.queues.values().forEach(queueServer -> {
	    	sender.sendMessage(DARK_GRAY + "====================================================");
	        sender.sendMessage(BLUE + "Status for " + WHITE + queueServer.getString("server.name") + BLUE + ":");
		    sender.sendMessage(BLUE + "Server-Online: " + WHITE + queueServer.getInteger("server.online"));
		    sender.sendMessage(BLUE + "Server-Max: " + WHITE + queueServer.getInteger("server.max"));
		    sender.sendMessage(BLUE + "Server-Whitelist: " + WHITE + (queueServer.getBoolean("server.whitelist") ? GREEN + "True" : RED + "False"));
		    sender.sendMessage(BLUE + "Running: " + WHITE + (queueServer.getBoolean("running") ? GREEN + "True" : RED + "False"));
		    sender.sendMessage(BLUE + "Size: " + WHITE + queueServer.getInteger("size"));
	    });
		return true;
	}

}
