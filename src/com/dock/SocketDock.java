package com.dock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.transfer.server.CommandSocket;

public class SocketDock {
	private static Map<String, CommandSocket> sSocketMap = new HashMap<String, CommandSocket>();
	
	/**
	 * register
	 * @param threadId
	 * @param commandSocket
	 */
	public static void register(String threadId, CommandSocket commandSocket){
		handleContext(HandleType.ADD, threadId, commandSocket);
	}
	
	/**
	 * unregister
	 * @param threadId
	 */
	public static void unregister(String threadId){
		handleContext(HandleType.REMOVE, threadId, null);
	}
	
	/**
	 * send message
	 * @param message
	 */
	public static void sendMessage(String message){
		Map<String, CommandSocket> map = new HashMap<String, CommandSocket>(sSocketMap);
		Set<Map.Entry<String, CommandSocket>> set = map.entrySet();
        for (Iterator<Map.Entry<String, CommandSocket>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, CommandSocket> entry = (Map.Entry<String, CommandSocket>) it.next();
            CommandSocket socket = entry.getValue();
            if(socket != null)
            	socket.sendMessage(message);
        }
	}
	
	private enum HandleType{
		ADD,
		REMOVE
	}
	private synchronized static void handleContext(HandleType handleType, String threadId, CommandSocket commandSocket){
		if(handleType == HandleType.ADD){
			sSocketMap.put(threadId, commandSocket);
		}else if(handleType == HandleType.REMOVE){
			sSocketMap.remove(threadId);
		}
	}
}
