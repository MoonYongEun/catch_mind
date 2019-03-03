package miniproject.catchmind;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WaitingRoomServer {
	private ServerSocket serverSocket;
	private ArrayList<WaitingRoomHandler>arrayList;
	private ArrayList<waitingRoomUserDTO> arUserList;
	private ArrayList<waitingRoomRCreateDTO> arRoomList;
	
	public WaitingRoomServer() {
		try {
			serverSocket = new ServerSocket(9500);
			System.out.println("서버 준비 완료....");
			arrayList = new ArrayList<WaitingRoomHandler>();
			arUserList = new ArrayList<waitingRoomUserDTO>();
			arRoomList = new ArrayList<waitingRoomRCreateDTO>();
			
			while(true) {
				Socket socket = serverSocket.accept();
				WaitingRoomHandler waitingroomhandler= new WaitingRoomHandler(socket, arrayList,arUserList, arRoomList);
				
				waitingroomhandler.start();
				arrayList.add(waitingroomhandler);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new WaitingRoomServer();
	}
}