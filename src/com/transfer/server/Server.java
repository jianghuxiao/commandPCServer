package com.transfer.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.util.Config;
import com.util.LogTool;

/**
 * server
 * @author Roy_Luo
 *
 */
public class Server {
	/**
	 * start listener
	 */
	public static void start(){
		Runnable runnableCommand = new Runnable(){
			public void run() {
				// TODO Auto-generated method stub
				try{
					/** command socket **/
					ServerSocket commandSocket = new ServerSocket(Config.COMMAND_PORT);
					while(true){
						Socket socket = commandSocket.accept();
						
						CommandSocket commandS = new CommandSocket();
						commandS.handler(socket);
					}
				}catch(Exception ex){
					LogTool.printException(ex);
				}
			}
		};
		
		(new Thread(runnableCommand)).start();
		
	}
	
}
