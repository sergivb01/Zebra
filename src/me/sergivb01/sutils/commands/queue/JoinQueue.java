package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.queue.QueueAPI;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;

public class JoinQueue implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "Only players nigger.");
			return false;
		}

		if(args.length == 0){
			sender.sendMessage(RED + "/joinqueue <server>");
			return false;
		}

		Player player = (Player)sender;
		String playerName = player.getName();

		QueueAPI.statusOf(playerName);

		String server = args[0];

		if(QueueAPI.statuses.containsKey(playerName)){
			Document document = QueueAPI.statuses.get(playerName);
			if(document.getBoolean("inqueue")){
				player.sendMessage(RED + "You are already in a queue!");
			}

			QueueAPI.addPlayer(playerName, server, getPriority(player));
			player.sendMessage(BLUE + "You have been added to " + WHITE + server + BLUE + " with a priority of " + WHITE + "1");
			return true;
		}

		player.sendMessage(RED + "Please, try again in few seconds.");

		return true;
	}

	private int getPriority(Player player){
		if(player.isOp()){
			return 1;
		}
		for(int i = 1; i <= 8; i++){
			if(player.hasPermission("priority." + i)){
				return i;
			}
		}
		return 9;
	}


}
