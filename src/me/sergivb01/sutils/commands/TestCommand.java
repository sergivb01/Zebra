package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import net.veilmc.hcf.deathban.Deathban;
import net.veilmc.util.PersistableLocation;
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

		MongoDBDatabase.setDeathban(player.getUniqueId(), new Deathban("Testerino", 5000, new PersistableLocation(player.getLocation())));

		return true;
	}


}
