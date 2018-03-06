package me.sergivb01.sutils.payload;

import me.sergivb01.sutils.database.redis.RedisDatabase;
import org.bson.Document;

import java.util.UUID;

public class PayloadSender{

	public static void requestPlayerDataUpdate(UUID playerUUID){
		sendPayload(
				new Document("type", "req-player-update")
						.append("playeruuid", playerUUID)
		);
	}

	private static void requestServerData(String server){
		sendPayload(
				new Document("type", "req-srv-update")
						.append("server", server)
		);
	}

	private static void sendPayload(Document document){
		RedisDatabase.getPublisher().write("payload;" +
				document.append("timestamp", System.currentTimeMillis()).toJson()
		);
	}


}
