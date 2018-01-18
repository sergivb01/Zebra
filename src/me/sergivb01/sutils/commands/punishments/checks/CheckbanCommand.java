package me.sergivb01.sutils.commands.punishments.checks;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.RED;

public class CheckbanCommand implements CommandExecutor {

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(args.length == 0){
			sender.sendMessage(RED + "Usage: '/checkban <player>'");
			return false;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if(target == null){
			sender.sendMessage(RED + "Unknown player.");
			return false;
		}

		if(MongoDBDatabase.isBlacklisted(target.getUniqueId())){
			sender.sendMessage(RED + target.getName() + " is currently blacklisted.");
			return true;
		}

		Document lastBan = MongoDBDatabase.getLastBan(target.getUniqueId());
		if(lastBan != null){
			sender.sendMessage(RED + "Last ban:");
			sender.sendMessage(lastBan.toString());
			return true;
		}
		sender.sendMessage(RED + target.getName() + " is not banned!");

		return true;
	}


}
