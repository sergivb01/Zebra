package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.queue.QueueAPI;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class StatusQueue implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "Only players nigger.");
			return false;
		}

		Player player = (Player)sender;
		String playerName = player.getName();

		QueueAPI.statusOf(playerName);

		if(QueueAPI.statuses.containsKey(playerName)){
			Document document = QueueAPI.statuses.get(playerName);
			if(document.getBoolean("inqueue")){
				player.sendMessage(QueueAPI.statuses.get(playerName).toJson());
				return true;
			}
			player.sendMessage(RED + "You are not in a queue!");
			return true;
		}

		player.sendMessage(RED + "Please, try again in few seconds.");

		return true;
	}


}
