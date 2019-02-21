package sukjae;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TestHandlerObject extends Thread {
	private ArrayList<TestHandlerObject>arrayList;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public TestHandlerObject(Socket socket , ArrayList<TestHandlerObject>arrayList) {
		this.socket = socket;
		this.arrayList = arrayList;
		
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		TestDTO testDTO = null;
		String nickName = null;
		
		UserDTO userDTO = null;
		String userName = null;
		
		RoomDTO roomDTO = null;
		String roomName = null;
		String roomPass = null;
		int roomPerson;
		
		while(true) {
	
			try {
				testDTO = (TestDTO)ois.readObject();
				userDTO = (UserDTO)ois.readObject();
				roomDTO = (RoomDTO)ois.readObject();
				
				
					if(testDTO.getCommand()== Info.JOIN) {
						nickName = testDTO.getNickName();
						TestDTO sendDTO = new TestDTO();
						
						sendDTO.setCommand(Info.SEND);
						sendDTO.setMessage(nickName+"님이 입장하였습니다");
						
						broadcast(sendDTO); 
						
					}else if(testDTO.getCommand()== Info.EXIT) {
						arrayList.remove(this);
						TestDTO sendDTO = new TestDTO();
						sendDTO.setCommand(Info.EXIT);
						
						UserDTO S_userDTO = new UserDTO();
						S_userDTO.setCommand(Info.EXIT);
						
						RoomDTO S_roomDTO = new RoomDTO();
						S_roomDTO.setCommand(Info.EXIT);
						
						oos.writeObject(sendDTO);
						oos.writeObject(S_userDTO);
						oos.writeObject(S_roomDTO);
						oos.flush();
		
						sendDTO.setCommand(Info.SEND);
						sendDTO.setMessage(nickName+"님이 퇴장하였습니다");
							
						broadcast(sendDTO);
	
						oos.close();
						ois.close();
						socket.close();

						break;
							
					}else if(testDTO.getCommand() == Info.SEND){
						TestDTO sendDTO = new TestDTO();
						sendDTO.setCommand(Info.SEND);
						sendDTO.setMessage("["+nickName+"] "+testDTO.getMessage());
						
						broadcast(sendDTO);
						broadcast(userDTO);
						broadcast(roomDTO);
						}
					
					if(userDTO.getCommand()== Info.JOIN) {
						userName = userDTO.getName();
						UserDTO S_userDTO = new UserDTO();
						S_userDTO.setName(userName);
						S_userDTO.setCommand(Info.JOIN);
						
						broadcast(S_userDTO);
						broadcast(roomDTO);
					}
					System.out.println("[1]roomDTO.getRoomName()"+roomDTO.getRoomName());
					
					if(roomDTO.getCommand() == Info.CREATE) {
						System.out.println("roomDTO.getRoomName()"+roomDTO.getRoomName());
						System.out.println("roomDTO.getRoomPass() "+roomDTO.getRoomPass());
						System.out.println("roomDTO.getPerson()"+roomDTO.getPerson());
						roomName = roomDTO.getRoomName();
						roomPass = roomDTO.getRoomPass();
						roomPerson = roomDTO.getPerson();
						
						RoomDTO S_roomDTO = new RoomDTO();
						S_roomDTO.setCommand(Info.CREATE);
						S_roomDTO.setRoomName(roomName);
						S_roomDTO.setRoomPass(roomPass);
						S_roomDTO.setPerson(roomPerson);
						
						broadcast(testDTO);
						broadcast(userDTO);
						broadcast(S_roomDTO);
					}
				
				
				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//while문
	}
	
	
	public void broadcast (TestDTO sendDTO){
		
		for(TestHandlerObject testHandlerObject : arrayList){
			try{
				testHandlerObject.oos.writeObject(sendDTO);
				testHandlerObject.oos.flush();
				
			}catch(IOException e){
				e.printStackTrace();

			}
		}
	}
	
	public void broadcast (UserDTO S_userDTO) {
		for(TestHandlerObject testHandlerObject : arrayList){
			try{
				testHandlerObject.oos.writeObject(S_userDTO);
				testHandlerObject.oos.flush();
				
			}catch(IOException e){
				e.printStackTrace();

			}
		}
	}
	
	public void broadcast (RoomDTO S_roomDTO) {
		for(TestHandlerObject testHandlerObject : arrayList) {
			try {
				testHandlerObject.oos.writeObject(S_roomDTO);
				testHandlerObject.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
