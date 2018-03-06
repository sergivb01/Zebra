package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class DeathHistoryCommand implements CommandExecutor{

	public boolean onCommand(final CommandSender sender, final Command comm, final String label, final String[] args){
		/*if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only players nigger.");
			return false;
		}
		Player player = (Player) sender;*/

		if(args.length < 1){
			sender.sendMessage(RED + "Usage: '/dhist <player>'");
			return false;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if(target == null){
			sender.sendMessage(RED + "No player named '" + args[0] + "' found.");
			return false;
		}

		sender.sendMessage(GREEN + "Last 20 deaths from " + GREEN + target.getName());
		for(Document doc : MongoDBDatabase.getRecentDeaths(target.getUniqueId())){
			sender.sendMessage(doc.toJson());
		}


		return true;
	}


}