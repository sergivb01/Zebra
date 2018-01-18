package me.sergivb01.sutils.commands.punishments.checks;

import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.utils.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.*;

public class AltsCommand implements CommandExecutor{

	public boolean onCommand (CommandSender sender, Command command, String s, String[] args) {
		if(args.length == 0){
			sender.sendMessage(RED + "Usage: '/alts <target>'");
			return false;
		}

		String target = args[0];
		boolean isIp = ip(args[0]);

		List<String> altsMap;
		if(isIp){
			sender.sendMessage(YELLOW + "Scanning alternative accounts for IP address '" + GREEN + target + YELLOW + "'...");
			altsMap = MongoDBDatabase.getIPAlts(target);
		}else{
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target);
			if(offlinePlayer == null){
				sender.sendMessage(RED + "Unknown player.");
				return false;
			}
			sender.sendMessage(YELLOW + "Scanning alternative accounts for player '" + GREEN + offlinePlayer.getName() + YELLOW + "'...");
			altsMap = MongoDBDatabase.getPlayerAlts(offlinePlayer.getUniqueId());
		}

		printDetails(altsMap, sender);
		return true;
	}

	private void printDetails(List<String> altsMap, CommandSender sender){
		FancyMessage fancyMessage = new FancyMessage("Alts found: ").color(GRAY);
		int i = 0;
		for(String str : altsMap){
			OfflinePlayer target = Bukkit.getOfflinePlayer(str);
			boolean online = target != null && target.isOnline();
			fancyMessage.then(str).
					color(online ? GREEN : RED)
					.tooltip(online ? (GREEN + "Online") : (RED + "Offline"));
			i++;
			if(i != altsMap.size()) {
				fancyMessage.then(", ").color(GRAY);
			}
		}
		fancyMessage.send(sender);
	}

	public static boolean ip(String text) {
		Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		Matcher m = p.matcher(text);
		return m.find();
	}

}
