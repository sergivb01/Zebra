package me.sergivb01.sutils.payload;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bson.Document;

public class PayloadSender{

	/*
		TODO:
			* Koths
			* Server data
			* Request server data
	 */

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
