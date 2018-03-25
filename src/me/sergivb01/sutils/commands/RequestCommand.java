package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.payload.PayloadSender;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class RequestCommand implements CommandExecutor{
	private static final Map<UUID, Long> COOLDOWNS;

	static{
		COOLDOWNS = new HashMap<>();
	}

	public boolean onCommand(final CommandSender sender, final Command comm, final String label, final String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "Only players nigger.");
			return false;
		}
		Player player = (Player) sender;

		if(args.length == 0){
			player.sendMessage(RED + "Usage: /request <reason...>");
			return false;
		}

		if(RequestCommand.COOLDOWNS.containsKey(player.getUniqueId())){
			if(System.currentTimeMillis() - RequestCommand.COOLDOWNS.get(player.getUniqueId()) < 100000L){
				player.sendMessage(RED + "You must wait before attempting to request staff assistance again.");
				return false;
			}
			RequestCommand.COOLDOWNS.remove(player.getUniqueId());
		}

		PayloadSender.sendRequest(player.getName(), StringUtils.join(args, " ").replace(";", ":"));
		player.sendMessage(GREEN + "Staff have been notified of your request.");
		RequestCommand.COOLDOWNS.put(player.getUniqueId(), System.currentTimeMillis());

		return true;
	}
}