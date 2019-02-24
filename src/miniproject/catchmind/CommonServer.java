package miniproject.catchmind;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CommonServer {
	private ServerSocket serverSocket;
	private ArrayList<CommonHandler>arCHandler;
	private ArrayList<waitingRoomUserDTO> arUserList;
	private ArrayList<waitingRoomRCreateDTO> arRoomList;
	private ArrayList<GameUserDTO> arGameUserList;
	
	public CommonServer() {
		try {
			serverSocket = new ServerSocket(9500);
			System.out.println("서버 준비 완료....");
			arCHandler = new ArrayList<CommonHandler>();
			arUserList = new ArrayList<waitingRoomUserDTO>();
			arRoomList = new ArrayList<waitingRoomRCreateDTO>();
			arGameUserList = new ArrayList<GameUserDTO>();
			
			while(true) {
				Socket socket = serverSocket.accept();
				CommonHandler commonhandler= new CommonHandler(socket, arCHandler,arUserList, arRoomList,arGameUserList);
				
				commonhandler.start();
				arCHandler.add(commonhandler);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new CommonServer();
	}
}