package miniproject.catchmind;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

public class CommonServer {
	private ServerSocket serverSocket, serverSocket2;
	private ArrayList<CommonHandler>arCHandler;
	private ArrayList<waitingRoomUserDTO> arUserList;
	private ArrayList<waitingRoomRCreateDTO> arRoomList;
	private ArrayList<GameUserDTO> arGameUserList;
	//private Socket socket;
	
	public CommonServer() {
		try {

			
			arCHandler = new ArrayList<CommonHandler>();
			arUserList = new ArrayList<waitingRoomUserDTO>();
			arRoomList = new ArrayList<waitingRoomRCreateDTO>();
			arGameUserList = new ArrayList<GameUserDTO>();

			serverSocket = new ServerSocket(9500);
			System.out.println("서버1 준비 완료....");

			
			while(true) {
				Socket socket = serverSocket.accept();//받아서 측정만 
				if(socket.isBound()) {
					System.out.println("9500에 정착되었다.");
				}else 
					//socket = serverSocket2.accept();
				System.out.println("server받아온 소켓의 = " +socket.getLocalPort());

				
				if(9500 == socket.getLocalPort()) {
					System.out.println("9500 port in");
					Socket socket2 = socket;
					System.out.println("9500 accept!!");
					CommonHandler commonhandler= new CommonHandler(socket2, arCHandler,arUserList, arRoomList,arGameUserList);
					commonhandler.start();
					arCHandler.add(commonhandler);
				}
				else if(9700 == socket.getLocalPort()) {
					System.out.println("9700 port in");
					Socket socket3 = socket;
					System.out.println("9700 accept!!");
					CommonHandler commonhandler= new CommonHandler(socket3, arCHandler,arUserList, arRoomList,arGameUserList);
					commonhandler.start();
					arCHandler.add(commonhandler);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new CommonServer();
	}
}