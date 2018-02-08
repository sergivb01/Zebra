package me.sergivb01.sutils.server;

import lombok.Getter;
import lombok.Setter;

public class Server {
	@Getter private String serverName;
	@Getter int[] tps = new int[2];
	@Getter @Setter int online = -1;
	@Getter @Setter int max = -1;
	@Getter @Setter boolean whitelist = false;

	public Server(String serverName){
		this.serverName = serverName;
	}



}
