package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.server.Cache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		Cache.getServerByName(args[0]).debug();
		return true;
	}


}
