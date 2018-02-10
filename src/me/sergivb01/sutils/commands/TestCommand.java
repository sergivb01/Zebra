package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.queue.QueueAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {

		String thing = args[0];
		String player = args[1];
		if(thing.equalsIgnoreCase("add")){
			QueueAPI.addPlayer(player, args[2], Integer.parseInt(args[3]));
		}else if(thing.equalsIgnoreCase("remove")){
			QueueAPI.removePlayer(player);
		}else{
			QueueAPI.statusOf(player);
		}

		return true;
	}


}
