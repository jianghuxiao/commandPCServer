package com.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.dock.SocketDock;
import com.transfer.server.Server;


public class App {
	/**
	 * launch app
	 * @param args
	 */
	public static void main(String[] args){
		Server.start();
		
		try{
			while(true){
				BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
				String message = strin.readLine();
				if(message == null || message.isEmpty())
					continue;
				
				SocketDock.sendMessage(message);
			}
		}catch(Exception ex){

		}
	}	
}
