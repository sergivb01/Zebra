package me.sergivb01.sutils.server;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class Server {
	@Getter private String serverName;
	@Getter int[] tps = new int[2];
	@Getter @Setter int online = -1;
	@Getter @Setter int max = -1;
	@Getter @Setter boolean whitelist = false;

	public Server(String serverName){
		this.serverName = serverName;
	}

	public void debug(){
		System.out.println("Debug data for " + serverName + "; " + serverName);
        System.out.println("TPS: " + Arrays.toString(tps));
        System.out.println("Online: " + online);
        System.out.println("Max: " + max);
        System.out.println("Whitelist: " + whitelist);
	}


}
