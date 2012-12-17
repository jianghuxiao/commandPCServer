package com.transfer.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.dock.ISocket;
import com.dock.SocketDock;
import com.transfer.command.Command;
import com.transfer.command.DataPackage;
import com.util.Config;
import com.util.LogTool;
import com.util._Random;

public class CommandSocket implements ISocket{
	private DataInputStream in;
	private DataOutputStream out;
	
	private String currentID;
	
	public CommandSocket(){
		currentID = _Random.createKey();
		SocketDock.register(currentID, this);
	}
	
	public void handler(final Socket socket){
		Runnable runnable = new Runnable(){
			public void run() {
					// TODO Auto-generated method stub
				try{
					in = new DataInputStream(socket.getInputStream());
					out = new DataOutputStream(socket.getOutputStream());
					
					handlerCommand(in, out);
					
					socket.close();
				}catch(Exception ex){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						LogTool.printException(ex);
					}
					
					SocketDock.unregister(currentID);
					
					LogTool.printException(ex);
				}
			}
		};
			
		(new Thread(runnable)).start();
	}
	
	/**
	 * handler command
	 * @param in
	 * @param out
	 * @throws IOException 
	 */
	private void handlerCommand(DataInputStream in, DataOutputStream out) throws IOException{
		while(true){
			String info = in.readUTF();
			CommandInfo commandInfo = parseCommand(info);
			if(commandInfo == null)
				return;
			
			//System.out.println("Command: " + info);
			
			switch(commandInfo.command){
				case Command.CONNECTING:
					writeHandle(DataPackage.createResponseInfo(Command.SUCCESS, null));
					break;
				case Command.CLOSE_CONNECT:
					break;
				case Command.OPEN_BROWER:
					break;
				case Command.CLOSE_BROWER:
					break;
				case Command.CLOSE_MACHINE:
					break;
				case Command.RESTART_MACHINE:
					break;
				case Command.OTHER:
					System.out.println(commandInfo.message);
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * write handle
	 * @param message
	 */
	private synchronized void writeHandle(String message){
		try{
			out.writeUTF(message);
			out.flush();
		}catch(Exception ex){
			
		}
	}
	
	private class CommandInfo{
		public int command = Command.COMMAND_WRONG;
		public String message = null;
	}
	
	/**
	 * parse command
	 * @param command
	 * @return
	 */
	private CommandInfo parseCommand(String command){
		CommandInfo info = new CommandInfo();

		String[] commandStr = command.split(Config.DELIMITER);
		if(commandStr.length < 2)
			return null;
		
		try{
			info.command = Integer.parseInt(commandStr[0]);
			info.message = commandStr[1];
		}catch(Exception e){
			info.command = Command.COMMAND_WRONG;
		}
		
		return info;
	}
	
	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		writeHandle(DataPackage.createResponseInfo(Command.OTHER, message));
	}
	
}
