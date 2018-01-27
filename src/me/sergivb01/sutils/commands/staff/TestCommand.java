package me.sergivb01.sutils.commands.staff;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("pLAYERS");
			return false;
		}

		Player player = (Player)sender;

		MongoDBDatabase.addPlayerDeath(player.getUniqueId(), new Document("test", "holaa").append("hola", 1234));
		return true;
	}


}
