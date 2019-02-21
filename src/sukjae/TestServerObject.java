package sukjae;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TestServerObject {
	private ServerSocket serverSocket;
	private ArrayList<TestHandlerObject>arrayList;
	
	public TestServerObject() {
		try {
			serverSocket = new ServerSocket(9000);
			System.out.println("서버 준비 완료....");
			arrayList = new ArrayList<TestHandlerObject>();
			
			while(true) {
				Socket socket = serverSocket.accept();
				TestHandlerObject testHandlerObject= new TestHandlerObject(socket,arrayList);
				
				testHandlerObject.start();
				arrayList.add(testHandlerObject);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new TestServerObject();
	}
}
