package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.server.ServerCache;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StatusCommand implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
		ServerCache.servers.forEach(srv -> {
			sender.sendMessage(c("&8=================================================="));
			sender.sendMessage(c("&6&lName: &f" + srv.getName()));
			sender.sendMessage(c("&6&lUp: &f" + srv.isUp()));
			sender.sendMessage(c("&6&lTPS0: &f" + srv.getTps()[0]));
			sender.sendMessage(c("&6&lTPS1: &f" + srv.getTps()[1]));
			sender.sendMessage(c("&6&lTPS2: &f" + srv.getTps()[2]));
			sender.sendMessage(c("&6&lPlayers: &f" + srv.getOnline() + "&7/&f" + srv.getMax()));
			sender.sendMessage(c("&6&lDonor: &f" + srv.isDonor()));
			sender.sendMessage(c("&6&lWhitelist: &f" + srv.isWhitelist()));
			sender.sendMessage(c("&6&lMute chat: &f" + srv.isMute()));
		});

		return true;
	}

	private String c(String str){
		return ChatColor.translateAlternateColorCodes('&', str);
	}

}
