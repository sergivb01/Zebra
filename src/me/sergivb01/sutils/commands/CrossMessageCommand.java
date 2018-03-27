package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.payload.PayloadSender;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class CrossMessageCommand implements CommandExecutor{

	public boolean onCommand(final CommandSender sender, final Command comm, final String label, final String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only players nigger.");
			return false;
		}
		Player player = (Player) sender;

		if(args.length < 2){
			sender.sendMessage(RED + "Usage: '/cmsg <player> <message>'");
			return false;
		}

		String target = args[0];
		String message = StringUtils.join(args, ' ', 1, args.length);


		sender.sendMessage(YELLOW + "Sent '" + WHITE + message + YELLOW + "' to " + GOLD + target);
		PayloadSender.sendCrossMessage(player.getName(), target, message);


		return true;
	}


}