package me.sergivb01.sutils.server;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ServerCache{
	public static List<Server> servers = new ArrayList<>();

	public static void createServerWithData(String server, Document doc){
		Server srv = new Server(server, -1, -1);
		srv.updateData(doc);
		servers.add(srv);
	}

	public static void handleData(String server, Document doc){
		Server srv = getServerByName(server);
		if(srv != null){
			srv.updateData(doc);
			return;
		}
		createServerWithData(server, doc);
	}

	public static Server getServerByName(String name){
		for(Server srv : servers){
			if(srv.getName().equalsIgnoreCase(name)) return srv;
		}
		return null;
	}

}
