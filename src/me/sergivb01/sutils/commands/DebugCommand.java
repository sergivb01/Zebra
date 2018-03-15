package me.sergivb01.sutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class DebugCommand implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
		if(args.length != 0){
			sender.sendMessage(RED + "/debug <target>");
		}
		final Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(RED + "No player named '" + args[0] + "' found online.");
			return false;
		}

		sender.sendMessage(BLUE + "Display name: " + WHITE + target.getDisplayName());
		sender.sendMessage(BLUE + "Name: " + WHITE + target.getName());
		sender.sendMessage(BLUE + "Custom name: " + target.getCustomName());
		sender.sendMessage(BLUE + "UUID: " + WHITE + target.getUniqueId());
		sender.sendMessage(BLUE + "UUID_str: " + WHITE + target.getUniqueId().toString());
		sender.sendMessage(BLUE + "Playerlist name: " + WHITE + target.getPlayerListName());
		sender.sendMessage(BLUE + "Address: " + WHITE + target.getAddress().toString());
		//TODO: Add more

		return true;
	}


}
