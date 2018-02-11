package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.queue.QueueAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class JoinQueue implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "Only players nigger.");
			return false;
		}

		Player player = (Player)sender;
		String playerName = player.getName();

		if(QueueAPI.isPlayerInQueue(playerName)){
			player.sendMessage(RED + "You are in a queue!");
			return true;
		}

		if(args.length == 0){
			sender.sendMessage(RED + "/joinqueue <server>");
			return false;
		}


		String server = args[0];

		QueueAPI.addPlayer(playerName, server, getPriority(player));
		player.sendMessage(BLUE + "You have been added to " + WHITE + server + BLUE + " with a priority of " + WHITE + "1");
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
