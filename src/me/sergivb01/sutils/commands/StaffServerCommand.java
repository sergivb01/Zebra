package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.ServerUtils;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class StaffServerCommand implements CommandExecutor{
	private ServerUtils instance;

	public StaffServerCommand(ServerUtils instance){
		this.instance = instance;
	}

	public boolean onCommand(final CommandSender sender, final Command comm, final String label, final String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(RED + "Only players nigger.");
			return false;
		}

		if(args.length <= 0){
			sender.sendMessage(RED + "Usage: '/staffserver <server>'");
			return false;
		}

		Player player = (Player) sender;

		final ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
		dataOutput.writeUTF("Connect");
		dataOutput.writeUTF(args[0]);
		player.sendMessage(YELLOW + "Sending you to " + GOLD + args[0]);
		player.sendPluginMessage(instance, "BungeeCord", dataOutput.toByteArray());

		return true;
	}
}