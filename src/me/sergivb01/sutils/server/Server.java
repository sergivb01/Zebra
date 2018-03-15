package me.sergivb01.sutils.server;

import lombok.Getter;
import org.bson.Document;

public class Server{
	@Getter
	private String name;
	@Getter
	private int players;
	@Getter
	private int max;
	@Getter
	private boolean online;

	public Server(String name, int players, int max){
		this.name = name;
		this.players = players;
		this.max = max;
		this.online = true;
	}

	public void updateData(Document payload){
		this.players = payload.getInteger("online");
		this.max = payload.getInteger("max");
		this.online = payload.getBoolean("online");
	}

}
