package miniproject.catchmind;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	private ServerSocket chatServerSocket;
	private ArrayList<ChatHandler> chatServerList;
	
	public ChatServer() {
		try {
			chatServerSocket=new ServerSocket(9500);//家南积己
			System.out.println("辑滚 霖厚 肯丰");
			chatServerList= new ArrayList<ChatHandler>();//list积己
			
			while(true) {
				Socket socket = chatServerSocket.accept();
				ChatHandler handler = new ChatHandler(socket,chatServerList);
				handler.start();
				chatServerList.add(handler);
			}//while
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new ChatServer();
	}
}
