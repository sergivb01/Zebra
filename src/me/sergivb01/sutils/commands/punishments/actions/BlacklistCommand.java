package me.sergivb01.sutils.commands.punishments.actions;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.RED;

public class BlacklistCommand implements CommandExecutor {

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(args.length == 0){
			sender.sendMessage(RED + "Usage: '/blacklist <player>'");
			return false;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if(target == null){
			sender.sendMessage(RED + "Unknown player.");
			return false;
		}

		MongoDBDatabase.setBlacklisted(target.getUniqueId(), true);

		RedisDatabase.getPublisher().write("kickrequest;" + target.getName() + ";" + ConfigUtils.SERVER_NAME + ";" + "\n&cYou have been blacklisted. \n\n Appeal at &7ts.myserver.net");

		sender.sendMessage(RED + target.getName() + " has been successfully blacklisted!");

		return true;
	}


}
