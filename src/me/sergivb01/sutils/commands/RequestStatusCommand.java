package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.payload.PayloadSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

public class RequestStatusCommand implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
		if(args.length <= 0){
			sender.sendMessage(RED + "Usage: '/rstatus <server>'");
			return true;
		}

		String srv = args[0];
		PayloadSender.sendReqData(srv);
		sender.sendMessage(YELLOW + "Sent request data payload to " + GOLD + srv + YELLOW + ".");


		return true;
	}


}
