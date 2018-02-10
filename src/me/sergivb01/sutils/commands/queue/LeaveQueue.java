package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.queue.QueueAPI;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import org.bson.Document;
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
		QueueAPI.statusOf(playerName);

		if(QueueAPI.statuses.containsKey(playerName)){
			Document document = QueueAPI.statuses.get(playerName);
			if(document.getBoolean("inqueue")){
				if(args.length != 0){
					if(args[0].equalsIgnoreCase("-f")){
						QueueAPI.removePlayer(playerName);
						player.sendMessage(BLUE + "You have left your previous queue!");
					}else if(args[0].equalsIgnoreCase("-no")){
						player.sendMessage(RED + "You still in queue!");
					}
				}

				new FancyMessage("Are you sure you want to leave ")
						.color(BLUE)
						.then(document.getString("server"))
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
			player.sendMessage(RED + "You are not in a queue!");
			return true;
		}

		player.sendMessage(RED + "Please, try again in few seconds.");

		return true;
	}


}
