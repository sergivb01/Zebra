package me.sergivb01.sutils.commands;

import me.sergivb01.sutils.payload.PayloadSender;
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

		return true;
	}


}
