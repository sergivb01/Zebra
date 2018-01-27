package me.sergivb01.sutils.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.sergivb01.sutils.ServerUtils;
import me.sergivb01.sutils.player.data.PlayerProfile;
import me.sergivb01.sutils.utils.ConfigUtils;
import net.veilmc.hcf.HCF;
import net.veilmc.hcf.faction.type.PlayerFaction;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MongoDBDatabase {
	private ServerUtils instance;
	private MongoClient mongoClient;
	private static MongoDatabase mongoDatabase;
	public static MongoCollection<Document> playercollection;
	public static MongoCollection<Document> factionCollection;

	public MongoDBDatabase (ServerUtils instance){
		this.instance = instance;
		init();
	}

	private void init() {
		MongoClientURI uri;
		if(ConfigUtils.MONGO_AUTH_ENABLED){
			uri = new MongoClientURI("mongodb://" + ConfigUtils.MONGO_USERNAME + ":" + ConfigUtils.MONGO_AUTH_PASSWORD +"@" + ConfigUtils.MONGO_HOST + ":" +  ConfigUtils.MONGO_PORT +"/?authSource=" + ConfigUtils.MONGO_DATABASE);
		}else{
			uri = new MongoClientURI("mongodb://" + ConfigUtils.MONGO_HOST + ":" +  ConfigUtils.MONGO_PORT + "/?authSource=" + ConfigUtils.MONGO_DATABASE);
		}
		mongoClient = new MongoClient(uri);
		mongoDatabase = mongoClient.getDatabase(ConfigUtils.MONGO_DATABASE);
		playercollection = mongoDatabase.getCollection("playerdata");
		factionCollection = mongoDatabase.getCollection("factions");

	}

	public static void saveFactionToDatabase(PlayerFaction playerFaction){
		List<String> allies = new ArrayList<>();
		playerFaction.getAlliedFactions().forEach(playerFaction1 -> allies.add(playerFaction.getName()));

		Document doc = new Document("uuid", playerFaction.getUniqueID())
		.append("name", playerFaction.getName())
		.append("players", playerFaction.getMembers().keySet())
		.append("leader", playerFaction.getLeader().getUniqueId())
		.append("allies", allies)
		.append("balance", playerFaction.getBalance())
		.append("dtr", playerFaction.getDeathsUntilRaidable());

		Document found = factionCollection.find(new Document("uuid", playerFaction.getUniqueID())).first();
		if(found != null){
			Bson updateOperation = new Document("$set", doc);
			factionCollection.updateOne(found, updateOperation);
		}else{
			factionCollection.insertOne(doc);
		}

	}

	public static void saveProfileToDatabase(PlayerProfile playerProfile, boolean online){
		PlayerFaction playerFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(playerProfile.getPlayer().getUniqueId());
		Player player = playerProfile.getPlayer();

		Document faction = new Document("faction", playerFaction != null);
		if(playerFaction != null){ //Player has a faction. Save faction data into doc
			faction.append("name", playerFaction.getName());
			faction.append("uuid", playerFaction.getUniqueID());
			saveFactionToDatabase(playerFaction);
		}

		//This includes ores, kills/deaths and more to add!
		Document profile = new Document("spawn-tokens", HCF.getInstance().getUserManager().getUser(player.getUniqueId()).getSpawnTokens())
				.append("kills", player.getStatistic(Statistic.PLAYER_KILLS))
				.append("deaths", player.getStatistic(Statistic.DEATHS))
				.append("ores",
						//Save player ores
						new Document("diamonds", player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE))
								.append("gold", player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE))
								.append("coal", player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE))
								.append("iron", player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE))
								.append("lapiz", player.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE))
								.append("redstone", player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE))
								.append("emerald", player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE))
				);

		//Includes all player profile (faction, )
		Document doc = new Document("uuid", player.getUniqueId())
		.append("nickname", player.getName())
		.append("address", playerProfile.getPlayerData().getAddress())
		.append("lastconn", System.currentTimeMillis())
		.append("server", ConfigUtils.SERVER_NAME)
		.append("version", playerProfile.getPlayerData().getPlayerVersion().toString())
		.append("online", online)
		.append(ConfigUtils.SERVER_NAME, new Document("profile", profile)
				.append("faction", faction)
		);

		Document found = playercollection.find(new Document("uuid", player.getUniqueId())).first();
		if(found != null){
			Bson updateOperation = new Document("$set", doc);
			playercollection.updateOne(found, updateOperation);
		}else{
			playercollection.insertOne(doc);
		}
	}

}
