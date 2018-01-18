package me.sergivb01.sutils.player.data;

import com.mongodb.client.FindIterable;
import lombok.Getter;
import me.sergivb01.sutils.database.mongo.MongoDBDatabase;
import me.sergivb01.sutils.enums.PunishmentType;
import me.sergivb01.sutils.player.Cache;
import me.sergivb01.sutils.utils.ConfigUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerProfile {
	@Getter private Player player;
	@Getter private PlayerData playerData;

	public PlayerProfile(Player player){
		this.player = player;
		this.playerData = new PlayerData(player);
	}

	public void setBlacklisted(boolean blacklisted){
		Document found = MongoDBDatabase.punishCollection.find(new Document("uuid", player.getUniqueId())).first();
		if(found != null){
			Bson updateOperation = new Document("$set", new Document("blacklisted", blacklisted));
			MongoDBDatabase.playercollection.updateOne(found, updateOperation);
		}
	}

	private List<Document> getPunishments(){
		List<Document> toReturn = new ArrayList<>();
		FindIterable<Document> findIterable = MongoDBDatabase.punishCollection.find(new Document("uuid", player.getUniqueId()));
		for(Document doc : findIterable){
			toReturn.add(doc);
		}
		return toReturn;
	}

	public void addPunishment(PunishmentType punishmentType, UUID sender, String reason, long duration){
		MongoDBDatabase.addPunishment(punishmentType, player.getUniqueId().toString(), sender, reason, duration, false);
	}

	public List<String> getPlayerAlts(){
		List<String> toReturn = new ArrayList<>();
		FindIterable<Document> findIterable = MongoDBDatabase.playercollection.find(new Document("address", player.getAddress().getHostString()));
		for(Document doc : findIterable){
			toReturn.add(doc.getString("nickname"));
		}
		return toReturn;
	}

	public void save(boolean online){
		Document doc = new Document("uuid", player.getUniqueId())
				.append("nickname", player.getName())
				.append("address", playerData.getAddress())
				.append("lastconn", System.currentTimeMillis())
				.append("version", playerData.getPlayerVersion().toString())
				.append("online", online)
				.append(ConfigUtils.SERVER_NAME, new Document("server", ConfigUtils.SERVER_NAME)
						.append("balance", playerData.getBalance())
				);

		Document found = MongoDBDatabase.playercollection.find(new Document("uuid", player.getUniqueId())).first();
		if(found != null){
			Bson updateOperation = new Document("$set", doc);
			MongoDBDatabase.playercollection.updateOne(found, updateOperation);
		}else{
			MongoDBDatabase.playercollection.insertOne(doc);
		}
	}

	public void remove(){
		Cache.removeProfile(player.getUniqueId());
	}

}
