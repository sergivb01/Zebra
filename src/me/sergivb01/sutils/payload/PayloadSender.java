package me.sergivb01.sutils.payload;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import net.veilmc.base.BasePlugin;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class PayloadSender{
	public static HashMap<String, Integer> reportedPlayers = new HashMap<>();

	public static void sendKoth(String koth){
		sendPayload(
				new Document("type", "koth")
						.append("koth", koth)
		);
	}

	public static void sendCrossMessage(String sender, String target, String message){
		sendPayload(
				new Document("type", "cmessage")
						.append("sender", sender)
						.append("target", target)
						.append("message", message)
		);
	}

	public static void sendStaffAbuse(String player, String str){
		sendPayload(
				new Document("type", "staffimportant")
						.append("player", player)
						.append("command", str)
		);
	}

	public static void sendData(boolean up){
		sendPayload(
				new Document("type", "data")
						.append("up", up)
						.append("online", Bukkit.getOnlinePlayers().size())
						.append("max", Bukkit.getMaxPlayers())
						.append("whitelist", Bukkit.hasWhitelist())
						.append("donor", up ? BasePlugin.getPlugin().getServerHandler().isDonorOnly() : true) //TODO: Find cleaner way
						.append("tps", new Document("tps0", Bukkit.spigot().getTPS()[0])
								.append("tps1", Bukkit.spigot().getTPS()[1])
								.append("tps2", Bukkit.spigot().getTPS()[2]))
						.append("chat", up ? BasePlugin.getPlugin().getServerHandler().isChatDisabled() : false)
						.append("antilag", up ? BasePlugin.getPlugin().getServerHandler().isDecreasedLagMode() : false)

		);
	}

	public static void sendMessage(String player, String message){
		sendPayload(
				new Document("type", "sendmessage")
						.append("player", player)
						.append("message", message)
		);
	}

	public static void sendReqData(String server){
		sendPayload(
				new Document("type", "reqdata")
						.append("req-server", server)
		);
	}

	public static void sendSwitch(String player, String action){
		sendPayload(
				new Document("type", "staffswitch")
						.append("player", player)
						.append("action", action)
		);
	}

	public static void sendRequest(String player, String message){
		sendPayload(
				new Document("type", "request")
						.append("player", player)
						.append("message", message)
		);
	}

	public static void sendReport(String sender, String target, String message){
		sendPayload(
				new Document("type", "report")
						.append("sender", sender)
						.append("count", reportedPlayers.getOrDefault(target, 1))
						.append("target", target)
						.append("message", message)
		);
	}

	public static void sendStaffchat(String playerName, String message){
		sendPayload(
				new Document("type", "staffchat")
						.append("player", playerName)
						.append("message", message)
		);
	}


	private static void sendPayload(Document document){
		document.append("timestamp", System.currentTimeMillis()).toJson();
		document.append("server", ConfigUtils.SERVER_NAME);

		RedisDatabase.getPublisher().write("payload;" + document.toJson());
	}


}
