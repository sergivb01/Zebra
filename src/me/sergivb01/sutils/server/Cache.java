package me.sergivb01.sutils.server;

import org.bson.Document;

import java.util.ArrayList;

public class Cache {
	public static ArrayList<Server> serverList = new ArrayList<>();

	public static Server getServerByName (String serverName){
		for(Server srv : serverList){
			if(srv.getServerName().equalsIgnoreCase(serverName)){
				return srv;
			}
		}
		return null;
	}

	public static void handleData(String serverName, String dataStr){
		Server server = getServerByName(serverName);
		System.out.println("1");
		if(server != null){
			System.out.println("2");
			Document document = Document.parse(dataStr);
			server.tps = (int[])document.getOrDefault("tps", new int[2]);
			server.setWhitelist((boolean)document.getOrDefault("whitelist", false));
			server.setOnline((Integer) document.getOrDefault("online", -1));
			server.setMax((Integer) document.getOrDefault("maxplayers", -1));
			server.debug();
		}else{
			System.out.println("3");
			Server srv = new Server(serverName);

			Document document = Document.parse(dataStr);
			srv.tps = (int[])document.getOrDefault("tps", new int[2]);
			srv.setWhitelist((boolean)document.getOrDefault("whitelist", false));
			srv.setOnline((Integer) document.getOrDefault("online", -1));
			srv.setMax((Integer) document.getOrDefault("maxplayers", -1));

			srv.debug();
			serverList.add(srv);
		}
	}

}
