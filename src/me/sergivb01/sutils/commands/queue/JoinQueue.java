package me.sergivb01.sutils.commands.queue;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.queue.QueueAPI;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;
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

		if(args.length == 0){
			player.sendMessage(RED + "/joinqueue <server>");
			return false;
		}

		String server = args[0];

		if(!RedisDatabase.getSubscriber().isBACKEND_UP()){
			player.sendMessage(RED + "Queues backend is currently down, sending you directly.");

			final ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
			dataOutput.writeUTF("Connect");
			dataOutput.writeUTF(server);
			player.sendMessage(YELLOW + "Sending you to " + GOLD + server);
			player.sendPluginMessage(RedisDatabase.getInstance(), "BungeeCord", dataOutput.toByteArray());
			return true;
		}



		QueueAPI.addPlayer(playerName, server, getPriority(player));
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
