package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.payload.PayloadSender;
import me.sergivb01.sutils.server.ServerCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
		PayloadSender.sendReport("sender123", "target123", "He is a hacker omg");
		PayloadSender.sendRequest("Player123", "There is a guy destroying outside my base!");
		PayloadSender.sendStaffchat("Staff123", "Hey how are u all guys?");
		PayloadSender.sendSwitch("Staff321", "joined");
		PayloadSender.sendSwitch("Staff321", "quit");

		ServerCache.servers.forEach(srv -> {
			System.out.print("=====================================================");
			System.out.print("Name: " + srv.getName());
			System.out.print("Up: " + srv.isUp());
			System.out.print("TPS0: " + srv.getTps()[0]);
			System.out.print("TPS1: " + srv.getTps()[1]);
			System.out.print("TPS2: " + srv.getTps()[2]);
			System.out.print("Players: " + srv.getOnline() + "/" + srv.getMax());
			System.out.print("Donor: " + srv.isDonor());
			System.out.print("Whitelist: " + srv.isWhitelist());
			System.out.print("Mute chat: " + srv.isMute());
		});

		return true;
	}


}
