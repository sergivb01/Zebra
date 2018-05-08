package me.sergivb01.sutils.utils;

import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InventoryUtils{

	public static String getInventoryAsJSON(Player player){ //Find cleaner way
		Map<String, String> invMap = new HashMap<>();

		for(int i = 0; i < 35; i++){
			invMap.put(String.valueOf(i), (player.getInventory().getItem(i) == null) ? Material.AIR.toString() : player.getInventory().getItem(i).getType().toString());
		}

		Map<String, String> armorMap = new HashMap<>();
		for(int i = 0; i < 4; i++){
			armorMap.put(String.valueOf(i), (player.getInventory().getArmorContents()[i] == null) ? Material.AIR.toString() : player.getInventory().getArmorContents()[i].getType().toString());
		}

		Document finalDoc = new Document("armor", armorMap)
				.append("inventory", invMap);
		return finalDoc.toJson();
	}

}
