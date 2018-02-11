package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.queue.QueueAPI;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class LeaveQueue implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "Only players nigger.");
			return false;
		}

		Player player = (Player)sender;
		String playerName = player.getName();
		String playerQueue = QueueAPI.getPlayerQueue(playerName);

		if(!QueueAPI.isPlayerInQueue(playerName)) {
			player.sendMessage(RED + "You are not in a queue!");
		}

		if (args.length != 0) {
			if (args[0].equalsIgnoreCase("-f")) {
				QueueAPI.removePlayer(playerName);
				player.sendMessage(BLUE + "You have left your previous queue!");
				return true;
			} else if (args[0].equalsIgnoreCase("-no")) {
				player.sendMessage(RED + "You still in queue!");
			}
			return true;
		}

		new FancyMessage("Are you sure you want to leave ")
				.color(BLUE)
				.then(playerQueue)
				.color(WHITE)
				.then(" queue?")
				.color(BLUE)
				.then(" [YES]")
				.color(GREEN)
				.command("/leavequeue -f")
				.then(" [NO]")
				.color(DARK_RED)
				.command("/leavequeue -no")
				.send(player);
		return true;
	}


}
