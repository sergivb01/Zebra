package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.queue.QueueAPI;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

public class Queues implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] strings) {
	    QueueAPI.statuses.values().forEach(queueServer -> {
		    Document queue = (Document)queueServer.get("queue");

	    	sender.sendMessage(DARK_GRAY + "====================================================");
	        sender.sendMessage(BLUE + "Status for " + WHITE + queueServer.getString("server") + BLUE + ":");
		    sender.sendMessage(BLUE + "Server-Online: " + WHITE + queueServer.getInteger("online"));
		    sender.sendMessage(BLUE + "Server-Max: " + WHITE + queueServer.getInteger("max"));
		    sender.sendMessage(BLUE + "Server-Whitelist: " + WHITE + (queueServer.getBoolean("whitelist") ? GREEN + "True" : RED + "False"));
		    sender.sendMessage(BLUE + "Running: " + WHITE + (queue.getBoolean("running") ? GREEN + "True" : RED + "False"));
		    sender.sendMessage(BLUE + "Size: " + WHITE + queue.getInteger("size"));
	    });
		return true;
	}

}
