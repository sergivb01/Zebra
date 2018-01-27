package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.base.user.ServerParticipator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class StaffChatCommand implements CommandExecutor{
	public boolean onCommand(final CommandSender sender, final Command comm, final String label, final String[] args) {
		ServerParticipator target;
		ServerParticipator participator = BasePlugin.getPlugin().getUserManager().getParticipator(sender);
		if (participator == null) {
			sender.sendMessage(RED + "You are not allowed to do this.");
			return true;
		}
		
		if (args.length <= 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(RED + "Usage: /sc <player|message...>");
				return true;
			}
			target = participator;
		} else {
			Player targetPlayer = Bukkit.getPlayerExact(args[0]);
			if (targetPlayer == null || !BaseCommand.canSee(sender, targetPlayer) || !sender.hasPermission("command.staffchat.others")) {
				RedisDatabase.getPublisher().write("staffchat;" + sender.getName() + ";" + ConfigUtils.SERVER_NAME + ";" + StringUtils.join(args, " ").replace(";", ":"));
				return true;
			}
			target = BasePlugin.getPlugin().getUserManager().getUser(targetPlayer.getUniqueId());
		}
		boolean newStaffChat = !target.isInStaffChat() || args.length >= 2 && Boolean.parseBoolean(args[1]);
		target.setInStaffChat(newStaffChat);
		sender.sendMessage(GREEN + "Staff chat mode of " + target.getName() + " set to " + newStaffChat + '.');
		return true;
	}
}