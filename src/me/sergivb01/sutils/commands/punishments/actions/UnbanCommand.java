package me.sergivb01.sutils.commands.punishments.actions;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static org.bukkit.ChatColor.RED;

public class UnbanCommand implements CommandExecutor {

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(args.length == 0){
			sender.sendMessage(RED + "Usage: '/unban <player>'");
			return false;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if(target == null){
			sender.sendMessage(RED + "Unknown player.");
			return false;
		}

		Document lastBan = MongoDBDatabase.getLastBan(target.getUniqueId());
		if(lastBan == null){
			sender.sendMessage(RED + "Target is not banned!");
			return false;
		}

		UUID senderUUID;
		if(!(sender instanceof Player)){
			senderUUID = UUID.fromString("2b0f25ab-a481-32c6-964e-038149dd16c8");
		}else{
			senderUUID = ((Player) sender).getUniqueId();
		}

		MongoDBDatabase.unbanPlayer(target.getUniqueId(), senderUUID);

		RedisDatabase.getPublisher().write("unbanmsg;" + target.getName() + ";" + ConfigUtils.SERVER_NAME + ";" + sender.getName());

		sender.sendMessage(RED + target.getName() + " has been successfully unbanned!");

		return true;
	}


}
