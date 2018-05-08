package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.payload.PayloadSender;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class ReportCommand implements CommandExecutor{
	private static final Map<UUID, Long> COOLDOWNS;

	static{
		COOLDOWNS = new HashMap<>();
	}

	public boolean onCommand(final CommandSender sender, final Command comm, final String label, final String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only players nigger.");
			return true;
		}
		Player player = (Player) sender;

		if(args.length < 2){
			sender.sendMessage(RED + "Usage: '/report <player> <reason...>'");
			return true;
		}


		final Player reported = Bukkit.getPlayer(args[0]);
		if(reported == null){
			player.sendMessage(RED + "No player named '" + args[0] + "' found online.");
			return true;
		}

		if(reported.equals(player)){ //Prevent users from reporting themselves
			player.sendMessage(RED + "You can't report yourself!");
			return true;
		}

		if(ReportCommand.COOLDOWNS.containsKey(player.getUniqueId())){
			if(System.currentTimeMillis() - ReportCommand.COOLDOWNS.get(player.getUniqueId()) < 100000L){
				player.sendMessage(RED + "You must wait before attempting to reporting a player again.");
				return true;
			}
			ReportCommand.COOLDOWNS.remove(player.getUniqueId());
		}


		PayloadSender.sendReport(player.getName(), reported.getName(), StringUtils.join(args, " ").replace(";", ":"));
		player.sendMessage(GREEN + "Staff have been notified of your player report.");
		ReportCommand.COOLDOWNS.put(player.getUniqueId(), System.currentTimeMillis());

		if(!PayloadSender.reportedPlayers.containsKey(reported.getName())){ //TODO: Clean up reported players feauture
			PayloadSender.reportedPlayers.put(reported.getName(), 0);
			return true;
		}
		PayloadSender.reportedPlayers.put(reported.getName(), PayloadSender.reportedPlayers.get(reported.getName()) + 1);

		return true;
	}
}