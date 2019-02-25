package miniproject.catchmind;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class CommonHandler extends Thread {
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ArrayList<CommonHandler>arCHandler;
	private ArrayList<waitingRoomUserDTO> arUserList;
	private ArrayList<waitingRoomRCreateDTO> arRoomList;
	private ArrayList<catchmind_ShapDTO> shapeDTOList;
	private ArrayList<GameUserDTO> arGameUserList;
	
	public CommonHandler(Socket socket,
			ArrayList<CommonHandler> arCHandler,
			ArrayList<waitingRoomUserDTO>  arUserList, 
			ArrayList<waitingRoomRCreateDTO> arRoomList,
			ArrayList<GameUserDTO> arGameUserList) {
		
		this.socket = socket;
		this.arCHandler = arCHandler;
		this.arUserList= arUserList;
		this.arRoomList = arRoomList;
		this.arGameUserList = arGameUserList;
		
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		//대기창 3개 
		WaitingRoomChattingDTO waitingroomchattingDTO = null;
		String nickName = null;
		
		waitingRoomUserDTO waitingroomuserDTO = null;
		String id = null;
		String userName = null;
		int score = 0 ;
		int indexNumber = 0;
		
		waitingRoomRCreateDTO waitingroomrcreateDTO = null;
		String roomName = null;
		String roomPass = null;
		int roomPerson = 0;
		
		//게임 2개
		ChatDTO chatHandlerDTO = null;
		String chatNickName = null;
		GameUserDTO gameuserDTO = null;
		String gameUserName =null;
		int point = 0;
		
		while(true) {
					try {

				//5개 서버로 부터 받기
				//대기창 3개 받기
						waitingroomchattingDTO = (WaitingRoomChattingDTO)ois.readObject();
						waitingroomuserDTO = (waitingRoomUserDTO)ois.readObject();
						waitingroomrcreateDTO = (waitingRoomRCreateDTO)ois.readObject();
						
						
				//게임 2개 받기		
						chatHandlerDTO=(ChatDTO)ois.readObject();
						shapeDTOList = (ArrayList<catchmind_ShapDTO>) ois.readObject();
						gameuserDTO = (GameUserDTO)ois.readObject();
						
				
				//유저 불러오기	----------------------------------------------------------------------	
						
						if(waitingroomchattingDTO.getCommand()== Info.JOIN && arUserList !=null) {
								
								for(int i =0; i<arUserList.size();i++) {
									userName = arUserList.get(i).getName();
									WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
									waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
									waitingroomuserDTO_send.setName(userName);
									waitingroomuserDTO_send.setCommand(Info.JOIN);
									waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
									ChatDTO chatDTO_send = new ChatDTO();
									oos.writeObject(waitingroomchattingDTO_send);
									oos.writeObject(waitingroomuserDTO_send);
									oos.writeObject(waitingroomrcreateDTO_send);
									oos.writeObject(chatDTO_send);
									oos.writeObject(shapeDTOList);
									oos.writeObject(gameuserDTO);
									
								}
								oos.flush();
							
						}
						
				//방몽록 불러오기---------------------------------------------------------------
						
						
						if(waitingroomrcreateDTO.getCommand() == Info.JOIN && arRoomList!=null) {
							
							for(int i =0; i<arRoomList.size();i++) {
								roomName = arRoomList.get(i).getRoomName();
								roomPass = arRoomList.get(i).getRoomPass();
								roomPerson = arRoomList.get(i).getPerson();
								WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
								waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
								waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
								waitingroomrcreateDTO_send.setRoomName(roomName);
								waitingroomrcreateDTO_send.setRoomPass(roomPass);
								waitingroomrcreateDTO_send.setPerson(roomPerson);
								waitingroomrcreateDTO_send.setCommand(Info.CREATE);
								ChatDTO chatDTO_send = new ChatDTO();
								oos.writeObject(waitingroomchattingDTO_send);
								oos.writeObject(waitingroomuserDTO_send);
								oos.writeObject(waitingroomrcreateDTO_send);
								oos.writeObject(chatDTO_send);
								oos.writeObject(shapeDTOList);
								oos.writeObject(gameuserDTO);
							
								//broadcast(waitingroomchattingDTO_send,waitingroomuserDTO_send);
							}
							oos.flush();
						}
						
						
						//arGameUserList.add(chatHandlerDTO);	//게임유저 리스트 추가
				//게임방에서 유저 추가-----------------------------------------------------------------------
						System.out.println(arGameUserList.size());
						
						if(gameuserDTO.getCommand()==Info.JOIN && arGameUserList != null) {
							System.out.println(" 들어온수치 : "+arGameUserList.size());
							for(int i =0; i<arGameUserList.size();i++) {
								gameUserName = arGameUserList.get(i).getName();
								point = arGameUserList.get(i).getPoint();
								arGameUserList.get(i).getOwner();
								System.out.println("에리이리스트에 저장된 Name 은? : "+gameUserName);
								
								WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
								waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
								waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
								ChatDTO chatDTO_send = new ChatDTO();
								GameUserDTO gameuserDTO_send = new GameUserDTO();
								gameuserDTO_send.setName(gameUserName);
								gameuserDTO_send.setPoint(point);
								gameuserDTO_send.setOwner(arGameUserList.get(i).getOwner());
								gameuserDTO_send.setCommand(Info.JOIN);
								
								System.out.println("gameuserDTO for문 몇번 돌았니?");
								
								oos.writeObject(waitingroomchattingDTO_send);
								oos.writeObject(waitingroomuserDTO_send);
								oos.writeObject(waitingroomrcreateDTO_send);
								oos.writeObject(chatDTO_send);
								oos.writeObject(shapeDTOList);
								oos.writeObject(gameuserDTO_send);
								
							}
							oos.flush();
						}
						
						
				//------------------------------------------------------------------------------------------
						if(waitingroomchattingDTO.getCommand()== Info.JOIN) {
							nickName = waitingroomchattingDTO.getNickName();
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							waitingroomchattingDTO_send.setNickName(nickName);
							waitingroomchattingDTO_send.setCommand(Info.SEND);
							waitingroomchattingDTO_send.setMessage(nickName+"님이 입장하였습니다");
							
							broadcast(waitingroomchattingDTO_send);	//1
							
							
						}else if(waitingroomchattingDTO.getCommand()== Info.EXIT) {
							
							arCHandler.remove(this);
							arUserList.remove(this);
							arRoomList.remove(this);
							arGameUserList.remove(this);
							
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							waitingroomchattingDTO_send.setNickName(nickName);
							waitingroomchattingDTO_send.setCommand(Info.EXIT);
							
							waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
							waitingroomuserDTO_send.setCommand(Info.EXIT);
							
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							waitingroomrcreateDTO_send.setCommand(Info.EXIT);
							
							oos.writeObject(waitingroomchattingDTO_send);
							oos.writeObject(waitingroomuserDTO_send);
							oos.writeObject(waitingroomrcreateDTO_send);
							oos.writeObject(chatHandlerDTO);
							oos.writeObject(shapeDTOList);
							oos.writeObject(gameuserDTO);
							oos.flush();
			
							waitingroomchattingDTO_send.setCommand(Info.SEND);
							waitingroomchattingDTO_send.setMessage(nickName+"님이 퇴장하였습니다");
							
							
							broadcast(waitingroomchattingDTO_send);
							broadcast(waitingroomuserDTO_send);
							broadcast(waitingroomrcreateDTO_send);
							broadcast(chatHandlerDTO);
							broadcast(shapeDTOList);
							broadcast(gameuserDTO);
							
							oos.close();
							ois.close();
							socket.close();
							
							break;
								
						}else if(waitingroomchattingDTO.getCommand() == Info.SEND){
		
							
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							waitingroomchattingDTO_send.setCommand(Info.SEND);
							waitingroomchattingDTO_send.setMessage("["+nickName+"] "+waitingroomchattingDTO.getMessage());
							
							
							broadcast(waitingroomchattingDTO_send);
							//broadcast(waitingroomuserDTO);
							//broadcast(waitingroomrcreateDTO);
							
						}else if(waitingroomchattingDTO.getCommand() == Info.WAIT){
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							waitingroomchattingDTO_send.setCommand(Info.WAIT);
							broadcast(waitingroomchattingDTO_send);
						}
						
						//--------------------------------------------------------
						
						
						if(waitingroomuserDTO.getCommand()== Info.JOIN) {
							arUserList.add(waitingroomuserDTO);				// arrayList 추가 사용자 리스트 카운트
							indexNumber = arUserList.size(); //유저의 인덱스번호 등록 너는 몇번째니
							id = waitingroomuserDTO.getId();
							userName = waitingroomuserDTO.getName();
							score = waitingroomuserDTO.getScore();
							
							waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
							waitingroomuserDTO_send.setName(userName);
							waitingroomuserDTO_send.setCommand(Info.JOIN);
							
							broadcast(waitingroomuserDTO_send);	
							
			
						}else if (waitingroomuserDTO.getCommand() ==Info.WAIT) {
							waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
							waitingroomuserDTO_send.setCommand(Info.WAIT);
							broadcast(waitingroomuserDTO_send);
						}
						
						//--------------------------------------------------------------
						
						if(waitingroomrcreateDTO.getCommand() == Info.JOIN) {
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							waitingroomrcreateDTO_send.setCommand(Info.WAIT);
							broadcast(waitingroomrcreateDTO_send);
							
							
						}else if(waitingroomrcreateDTO.getCommand() == Info.CREATE) {		
							arRoomList.add(waitingroomrcreateDTO);				//arrayList 추가  방 만들기 카운트 
			
							roomName = waitingroomrcreateDTO.getRoomName();
							roomPass = waitingroomrcreateDTO.getRoomPass();
							roomPerson = waitingroomrcreateDTO.getPerson();
							
							//UserArrayList 에서 삭제
							//arUserList.remove(indexNumber);
							
							
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							waitingroomrcreateDTO_send.setCommand(Info.CREATE);
							waitingroomrcreateDTO_send.setRoomName(roomName);
							waitingroomrcreateDTO_send.setRoomPass(roomPass);
							waitingroomrcreateDTO_send.setPerson(roomPerson);
							
							broadcast(waitingroomrcreateDTO_send);
							

						}else if(waitingroomrcreateDTO.getCommand() == Info.WAIT) {
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							waitingroomrcreateDTO_send.setCommand(Info.WAIT);
							broadcast(waitingroomrcreateDTO_send);
						}
						
						//-------------------------------------------------------------------
						
						if(chatHandlerDTO.getCommand()==Info.JOIN) {
							chatNickName = gameuserDTO.getName();

							ChatDTO chatSendDTO= new ChatDTO();
							chatSendDTO.setCommand(Info.SEND);
							chatSendDTO.setMessage("["+chatNickName+"]님이 입장하였습니다,  "+"매너채팅 하시길 바랍니다");
							
							broadcast(chatSendDTO);
							broadcast(shapeDTOList);

							
						}else if(chatHandlerDTO.getCommand()==Info.EXIT) {
							
							//chatHandlerList.remove(this);
							
							ChatDTO chatSendDTO= new ChatDTO();
							chatSendDTO.setCommand(Info.EXIT);
							GameUserDTO gameuserDTO_send = new GameUserDTO();
							gameuserDTO_send.setCommand(Info.EXIT);
							
							oos.writeObject(waitingroomchattingDTO);
							oos.writeObject(waitingroomuserDTO);
							oos.writeObject(waitingroomrcreateDTO);
							oos.writeObject(chatSendDTO);
							oos.writeObject(shapeDTOList);
							oos.writeObject(gameuserDTO_send);
							oos.flush();
							
							chatSendDTO.setCommand(Info.SEND);
							chatSendDTO.setMessage(chatNickName+"님이 퇴장하였습니다");
							
							broadcast(waitingroomchattingDTO);
							broadcast(waitingroomuserDTO);
							broadcast(waitingroomrcreateDTO);
							broadcast(chatSendDTO);
							broadcast(shapeDTOList);
							broadcast(gameuserDTO_send);
							
							
						}else if(chatHandlerDTO.getCommand()==Info.SEND) {
							ChatDTO chatSendDTO= new ChatDTO();
							chatSendDTO.setCommand(Info.SEND);
							chatSendDTO.setMessage("["+chatNickName+"] "+chatHandlerDTO.getMessage());
							
							broadcast(waitingroomchattingDTO);
							broadcast(waitingroomuserDTO);
							broadcast(waitingroomrcreateDTO);
							broadcast(chatSendDTO);
							broadcast(shapeDTOList);

							
						}else if(chatHandlerDTO.getCommand()==Info.WAIT) {
							ChatDTO chatSendDTO= new ChatDTO();
							
							broadcast(chatSendDTO);
							broadcast(shapeDTOList);
							
						}else if(chatHandlerDTO.getCommand()==Info.READY) {
			
							ChatDTO chatDTO_send = new ChatDTO();
							chatDTO_send.setCommand(Info.READY);
							chatDTO_send.setReadyCount(chatHandlerDTO.getReadyCount());
							
							broadcast(chatDTO_send);
							broadcast(shapeDTOList);
							
						}else if(chatHandlerDTO.getCommand()==Info.START) {
							ChatDTO chatDTO_send = new ChatDTO();
							chatDTO_send.setCommand(Info.START);
							chatDTO_send.setStartCount(chatHandlerDTO.getStartCount());
							
							broadcast(chatDTO_send);
							broadcast(shapeDTOList);
							
						}else if(chatHandlerDTO.getCommand()==Info.ANSWER) {
							ChatDTO chatDTO_send = new ChatDTO();
							chatDTO_send.setCommand(Info.ANSWER);
							
							broadcast(chatDTO_send);
							broadcast(shapeDTOList);
							
						}
						
						//-----------------------------------------------------------------------
						
						if(gameuserDTO.getCommand()==Info.JOIN) {
							
							gameUserName = gameuserDTO.getName();			//이름 저장
							point = gameuserDTO.getPoint();				//점수 저장

							//arGameUserList.add(gameuserDTO);	//게임유저 Array 리스트 추가 어레이리스트에 이름이 들어가기 위해서
							GameUserDTO gameuserDTO_send = new GameUserDTO();
							gameuserDTO_send.setName(gameUserName);
							gameuserDTO_send.setPoint(point);

							if(arGameUserList.size()==0)gameuserDTO_send.setOwner("방장");
							else gameuserDTO_send.setOwner("유저");
							
							arGameUserList.add(gameuserDTO_send);
							gameuserDTO_send.setCommand(Info.JOIN);
							
							broadcast(gameuserDTO_send);
							
						}else if(gameuserDTO.getCommand()==Info.WAIT) {
						
							GameUserDTO gameuserDTO_send = new GameUserDTO();

							broadcast(gameuserDTO_send);
						}
						
						//------------------------------------------------------------------------------
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
		
		
		}//while문
	}//run


	public void broadcast (WaitingRoomChattingDTO waitingroomchattingDTO_send){
		
		for(CommonHandler commonhandler : arCHandler){
			try{
				commonhandler.oos.writeObject(waitingroomchattingDTO_send);
				commonhandler.oos.flush();
				
			}catch(IOException e){
				e.printStackTrace();
	
			}
		}
	}
	
	public void broadcast (waitingRoomUserDTO waitingroomuserDTO_send) {
		for(CommonHandler commonhandler : arCHandler){
			try{
				commonhandler.oos.writeObject(waitingroomuserDTO_send);
				commonhandler.oos.flush();
			}catch(IOException e){
				e.printStackTrace();
			}
		}		
	}
	public void broadcast (WaitingRoomChattingDTO waitingroomchattingDTO,
							waitingRoomUserDTO waitingroomuserDTO_send){
		for(CommonHandler commonhandler : arCHandler){
			try{
				commonhandler.oos.writeObject(waitingroomchattingDTO);
				commonhandler.oos.writeObject(waitingroomuserDTO_send);
				commonhandler.oos.flush();
				
			}catch(IOException e){
				e.printStackTrace();
	
			}
		}
	}
	public void broadcast (waitingRoomRCreateDTO waitingroomrcreateDTO_send) {
		
		for(CommonHandler commonhandler : arCHandler) {
			try {
				commonhandler.oos.writeObject(waitingroomrcreateDTO_send);
				commonhandler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void broadcast(ArrayList<catchmind_ShapDTO> shapeDTOList) {
		for(CommonHandler commonhandler:arCHandler) {
			try {
				commonhandler.oos.writeObject(shapeDTOList);
				commonhandler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		
	}

	public void broadcast(ChatDTO chatsendDTO) {
		for(CommonHandler commonhandler:arCHandler) {
			try {
				commonhandler.oos.writeObject(chatsendDTO);
				commonhandler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void broadcast(GameUserDTO gameuserDTO_send) {
		for(CommonHandler commonhandler:arCHandler) {
			try {
				commonhandler.oos.writeObject(gameuserDTO_send);
				commonhandler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
}


