package miniproject.catchmind;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WaitingRoomHandler extends Thread {
	private ArrayList<WaitingRoomHandler>arrayList;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ArrayList<waitingRoomUserDTO> arUserList;
	private ArrayList<waitingRoomRCreateDTO> arRoomList;
	private int sw;
	
	public WaitingRoomHandler(Socket socket , ArrayList<WaitingRoomHandler>arrayList,ArrayList<waitingRoomUserDTO>arUserList) {
		this.socket = socket;
		this.arrayList = arrayList;
		this.arUserList= arUserList;
		//arUserList = new ArrayList<waitingRoomUserDTO>();
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		WaitingRoomChattingDTO waitingroomchattingDTO = null;
		String nickName = null;
		
		waitingRoomUserDTO waitingroomuserDTO = null;
		String userName = null;
		
		//waitingRoomRCreateDTO waitingroomrcreateDTO = null;
		//String roomName = null;
		//String roomPass = null;
		//int roomPerson;
		
		while(true) {
			try {
				
				waitingroomchattingDTO = (WaitingRoomChattingDTO)ois.readObject();
				waitingroomuserDTO = (waitingRoomUserDTO)ois.readObject();
				//waitingroomrcreateDTO = (waitingRoomRCreateDTO)ois.readObject();
				
				
				
				//System.out.println(arUserList.get(0).getName());
				//System.out.println(waitingroomuserDTO.getName());
				
				if(waitingroomchattingDTO.getCommand_A()== Info.JOIN && waitingroomuserDTO.getCommand()== Info.JOIN) {
					if(arUserList.size() > 0) {
						
						for(int i =0; i<arUserList.size();i++) {
					
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							userName = arUserList.get(i).getName();
							waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
							waitingroomuserDTO_send.setName(userName);
							waitingroomuserDTO_send.setCommand(Info.JOIN);
							oos.writeObject(waitingroomchattingDTO_send);
							oos.writeObject(waitingroomuserDTO_send);
						
							//broadcast(waitingroomchattingDTO_send,waitingroomuserDTO_send);
						}
						
					}
				}
				
				if(waitingroomchattingDTO.getCommand_A()== Info.JOIN) {
						nickName = waitingroomchattingDTO.getNickName();
						WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
						waitingroomchattingDTO_send.setNickName(nickName);
						waitingroomchattingDTO_send.setCommand_A(Info.SEND);
						waitingroomchattingDTO_send.setMessage(nickName+"님이 입장하였습니다");
						broadcast(waitingroomchattingDTO_send); 

						
					}else if(waitingroomchattingDTO.getCommand_A()== Info.EXIT) {
						
						arrayList.remove(this);
						arUserList.remove(this);
						
						WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
						waitingroomchattingDTO_send.setNickName(nickName);
						waitingroomchattingDTO_send.setCommand_E(Info.EXIT);
						
						waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
						waitingroomuserDTO_send.setCommand(Info.EXIT);
						
						//waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
						//waitingroomrcreateDTO_send.setCommand(Info.EXIT);
						
						oos.writeObject(waitingroomchattingDTO_send);
						oos.writeObject(waitingroomuserDTO_send);
						//oos.writeObject(waitingroomrcreateDTO_send);
						oos.flush();
		
						waitingroomchattingDTO_send.setCommand_A(Info.SEND);
						waitingroomchattingDTO_send.setMessage(nickName+"님이 퇴장하였습니다");
						
						
						broadcast(waitingroomchattingDTO_send);
						broadcast(waitingroomuserDTO);
						//broadcast(waitingroomrcreateDTO);
	
						oos.close();
						ois.close();
						socket.close();

						break;
							
					}else if(waitingroomchattingDTO.getCommand_A() == Info.SEND){
	
						
						WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
						waitingroomchattingDTO_send.setCommand_A(Info.SEND);
						waitingroomchattingDTO_send.setMessage("["+nickName+"] "+waitingroomchattingDTO.getMessage());
						
						
						broadcast(waitingroomchattingDTO_send);
						//broadcast(waitingroomuserDTO);
						//broadcast(waitingroomrcreateDTO);
						
					}
					
					
					
					
					
				
					if(waitingroomuserDTO.getCommand()== Info.JOIN) {
						arUserList.add(waitingroomuserDTO);

						userName = waitingroomuserDTO.getName();
						waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
						waitingroomuserDTO_send.setName(userName);
						waitingroomuserDTO_send.setCommand(Info.JOIN);
						
						broadcast(waitingroomuserDTO_send);
		
					}else if (waitingroomuserDTO.getCommand()==Info.WAIT) {
						waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
						waitingroomuserDTO_send.setCommand(Info.WAIT);
						broadcast(waitingroomuserDTO_send);
					}
					
					/*
					if(waitingroomrcreateDTO.getCommand() == Info.CREATE) {
		
						roomName = waitingroomrcreateDTO.getRoomName();
						roomPass = waitingroomrcreateDTO.getRoomPass();
						roomPerson = waitingroomrcreateDTO.getPerson();
						
						waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
						waitingroomrcreateDTO_send.setCommand(Info.CREATE);
						waitingroomrcreateDTO_send.setRoomName(roomName);
						waitingroomrcreateDTO_send.setRoomPass(roomPass);
						waitingroomrcreateDTO_send.setPerson(roomPerson);
						
						broadcast(waitingroomchattingDTO);
						broadcast(waitingroomuserDTO);
						broadcast(waitingroomrcreateDTO_send);
					}
					*/
				

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//while문
	}
	
	
	public void broadcast (WaitingRoomChattingDTO waitingroomchattingDTO_send){
		
		for(WaitingRoomHandler waitingroomhandler : arrayList){
			try{
				waitingroomhandler.oos.writeObject(waitingroomchattingDTO_send);
				waitingroomhandler.oos.flush();
				
			}catch(IOException e){
				e.printStackTrace();

			}
		}
	}

	public void broadcast (waitingRoomUserDTO waitingroomuserDTO_send) {
		
		
			//for(WaitingRoomChattingDTO dto : arUserList) {
				for(WaitingRoomHandler waitingroomhandler : arrayList){
					try{
						waitingroomhandler.oos.writeObject(waitingroomuserDTO_send);
						waitingroomhandler.oos.flush();
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			//}
			
}
public void broadcast (WaitingRoomChattingDTO waitingroomchattingDTO,
							waitingRoomUserDTO waitingroomuserDTO_send){

		
		for(WaitingRoomHandler waitingroomhandler : arrayList){
			try{
				waitingroomhandler.oos.writeObject(waitingroomchattingDTO);
				waitingroomhandler.oos.writeObject(waitingroomuserDTO_send);
				waitingroomhandler.oos.flush();
				
			}catch(IOException e){
				e.printStackTrace();

			}
		}
	}

	
/*


	public void broadcast (waitingRoomRCreateDTO waitingroomrcreateDTO_send) {
		//if(waitingroomrcreateDTO_send !=null)arRoomList.add(waitingroomrcreateDTO_send);
		
		for(WaitingRoomHandler waitingroomhandler : arrayList) {
			try {
				//for(waitingRoomRCreateDTO dto : arRoomList) {
					waitingroomhandler.oos.writeObject(waitingroomrcreateDTO_send);
				//}
				waitingroomhandler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	*/
}