package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.queue.QueueAPI;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class LeaveQueue implements CommandExecutor{
	private List<String> players = new ArrayList<>();

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "Only players nigger1.");
			return false;
		}

		Player player = (Player)sender;
		String playerName = player.getName();
		if(args.length <= 0){
			new FancyMessage("Are you sure you want to leave ")
					.color(BLUE)
					.then(" the queue?")
					.color(BLUE)
					.then(" [YES]")
					.color(GREEN)
					.tooltip(GREEN + "Click to leave the queue")
					.command("/leavequeue -f")
					.then(" [NO]")
					.color(DARK_RED)
					.tooltip(RED + "Click to remain in the queue")
					.command("/leavequeue -no")
			players.add(playerName);
			Bukkit.getScheduler().scheduleSyncDelayedTask(RedisDatabase.getInstance(), ()->{
				players.remove(playerName);
			}, 5 * 20L);
			return true;
		}
		if(args[0].equalsIgnoreCase("-f") && players.contains(playerName)) {
			QueueAPI.removePlayer(playerName);
			players.remove(playerName);
		}else if(players.contains(playerName)){
			player.sendMessage(BLUE + "You will remain in the queue");
			players.remove(playerName);
		}


		return true;
	}


}
