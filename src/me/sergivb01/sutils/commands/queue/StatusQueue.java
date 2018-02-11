package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.queue.QueueAPI;
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

		player.sendMessage(QueueAPI.isPlayerInQueue(playerName).toJson());

		return true;
	}


}
