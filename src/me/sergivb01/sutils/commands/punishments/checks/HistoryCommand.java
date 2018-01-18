package me.sergivb01.sutils.commands.punishments.checks;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.RED;

public class HistoryCommand implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(args.length == 0){
			sender.sendMessage(RED + "Usage: '/hist <target>'");
			return false;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if(target == null){
			sender.sendMessage(RED + "Unknown player.");
			return false;
		}

		for(Document doc : MongoDBDatabase.getPunishmentsByPunished(target.getUniqueId())){
			sender.sendMessage(doc.toString());
		}

		return true;
	}

}
