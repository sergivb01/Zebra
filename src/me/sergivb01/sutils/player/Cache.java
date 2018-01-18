package me.sergivb01.sutils.player;

import me.sergivb01.sutils.player.data.PlayerProfile;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cache {
	public static Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();

	public static void addProfile(UUID uuid, PlayerProfile playerProfile){
		if(!playerProfiles.containsKey(uuid)){
			playerProfiles.put(uuid, playerProfile);
		}
	}

	public static PlayerProfile getPlayerProfile(UUID uuid){
		return playerProfiles.get(uuid);
	}

	public static PlayerProfile getPlayerProfile(Player player){
		return playerProfiles.get(player.getUniqueId());
	}

	public static void removeProfile(UUID uuid){
		if(playerProfiles.containsKey(uuid)){
			playerProfiles.remove(uuid);
		}
	}

	public static void removeProfile(Player player){
		if(playerProfiles.containsKey(player.getUniqueId())){
			playerProfiles.remove(player.getUniqueId());
		}
	}


}
