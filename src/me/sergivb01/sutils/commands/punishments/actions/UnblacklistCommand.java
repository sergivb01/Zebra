package me.sergivb01.sutils.commands.punishments.actions;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.RED;

public class UnblacklistCommand implements CommandExecutor {

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(args.length == 0){
			sender.sendMessage(RED + "Usage: '/unblacklist <player>'");
			return false;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if(target == null){
			sender.sendMessage(RED + "Unknown player.");
			return false;
		}

		MongoDBDatabase.setBlacklisted(target.getUniqueId(), false);
		sender.sendMessage(RED + target.getName() + " has been successfully un-blacklisted!");
		return true;
	}


}
