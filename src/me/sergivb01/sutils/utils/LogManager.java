package me.sergivb01.sutils.utils;

import me.sergivb01.sutils.ServerUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager{
	private ServerUtils instance;
	private File file;

	public LogManager(ServerUtils plugin){
		this.instance = plugin;
		System.out.println(plugin.getDataFolder() + "");
		file = new File(plugin.getDataFolder() + "/stafflog.txt");

		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}

	}

	public void addMessage(String message){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.append(message);
			bw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void formatMessage(String name, String msg){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
		String date = dateFormat.format(new Date());
		String finalMessage = date + " - " + name + ": " + msg + "\n";
		addMessage(finalMessage);
	}


}