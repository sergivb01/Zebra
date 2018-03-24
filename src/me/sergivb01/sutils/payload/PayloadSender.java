package me.sergivb01.sutils.payload;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import net.veilmc.base.BasePlugin;
import org.bson.Document;
import org.bukkit.Bukkit;

public class PayloadSender{

	public static void sendKoth(String koth){
		sendPayload(
				new Document("type", "koth")
					.append("koth", koth)
		);
	}

	public static void sendData(boolean up){
		double tps[] = Bukkit.spigot().getTPS();
		sendPayload(
				new Document("type", "data")
					.append("up", up)
					.append("online", Bukkit.getOnlinePlayers().size())
					.append("max", Bukkit.getMaxPlayers())
					.append("whitelist", Bukkit.hasWhitelist())
					.append("donor", BasePlugin.getPlugin().getServerHandler().isDonorOnly())
					.append("tps", new Document("tps0", tps[0])
							.append("tps1", tps[1])
							.append("tps2", tps[2]))
					.append("chat", BasePlugin.getPlugin().getServerHandler().isChatDisabled())
					.append("antilag", BasePlugin.getPlugin().getServerHandler().isDecreasedLagMode())

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
