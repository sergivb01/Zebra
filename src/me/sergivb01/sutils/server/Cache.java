package me.sergivb01.sutils.server;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cache {
	public static List<Server> serverList = new ArrayList<>();

	public static Server getServerByName (String serverName){
		Optional<Server> serverOptional = serverList.stream().filter(srv -> srv.getServerName().equalsIgnoreCase(serverName)).findFirst();
		return serverOptional.orElse(null);
	}

	public static void handleData(String serverName, String dataStr){
		Server server = getServerByName(serverName);
		if(server != null){
			Document document = Document.parse(dataStr);
			server.tps = (int[])document.getOrDefault("tps", new int[2]);
			server.setWhitelist((boolean)document.getOrDefault("whitelist", false));
			server.setOnline((Integer) document.getOrDefault("online", -1));
			server.setMax((Integer) document.getOrDefault("maxplayers", -1));
		}else{
			Server srv = new Server(serverName);

			Document document = Document.parse(dataStr);
			srv.tps = (int[])document.getOrDefault("tps", new int[2]);
			srv.setWhitelist((boolean)document.getOrDefault("whitelist", false));
			srv.setOnline((Integer) document.getOrDefault("online", -1));
			srv.setMax((Integer) document.getOrDefault("maxplayers", -1));

			serverList.add(srv);
		}
	}

}
