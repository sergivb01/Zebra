package me.sergivb01.sutils.server;

import lombok.Getter;
import org.bson.Document;

public class Server{
	@Getter
	private String name;
	@Getter
	private int online;
	@Getter
	private int max;
	@Getter
	private boolean up;
	@Getter
	private boolean whitelist;
	@Getter
	private boolean donor;
	@Getter
	private boolean mute;
	@Getter
	private boolean lag;
	@Getter
	private double tps[] = new double[3];

	public Server(String name, int players, int max){
		this.name = name;
		this.online = players;
		this.max = max;
		this.up = true;
		this.whitelist = false;
		this.donor = false;
		this.mute = false;
		this.lag = false;
	}

	public void updateData(Document payload){
		this.up = payload.getBoolean("up");
		this.online = payload.getInteger("online");
		this.max = payload.getInteger("max");
		this.whitelist = payload.getBoolean("whitelist");
		this.donor = payload.getBoolean("donor");
		this.tps[0] = (double)payload.getOrDefault("tps.tps0", 0.0);
		this.tps[1] = (double)payload.getOrDefault("tps.tps1", 0.0);
		this.tps[2] = (double)payload.getOrDefault("tps.tps2", 0.0);
		this.mute = payload.getBoolean("chat");
		this.lag = payload.getBoolean("antilag");
	}

}
