package me.sergivb01.sutils.commands.staff;

import me.sergivb01.sutils.ServerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		ServerUtils.broadcastKoth(args[0]);
		return true;
	}


}
