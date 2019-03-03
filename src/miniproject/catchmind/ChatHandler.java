package miniproject.catchmind;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ChatHandler extends Thread{
	private Socket chatHandlerSocket;
	private ArrayList <ChatHandler> chatHandlerList;
	private ArrayList<catchmind_ShapDTO> shapeDTOList;
	private ObjectInputStream chatHandlerOis;
	private ObjectOutputStream chatHandlerOos;
	private int ready; //★추가한내용
	private int start; //★추가한내용
	
	public ChatHandler(Socket chatHandlerSocket,ArrayList<ChatHandler> chatHandlerList )throws IOException{
		this.chatHandlerSocket=chatHandlerSocket;
		this.chatHandlerList=chatHandlerList;
		
		chatHandlerOos=new ObjectOutputStream(chatHandlerSocket.getOutputStream());
		chatHandlerOis=new ObjectInputStream(chatHandlerSocket.getInputStream());
	}//생성자 종료
	
	@Override
	public void run() {
		ChatDTO chatHandlerDTO=null;
		String chatNickName=null;
		while(true) {
			try {				
				chatHandlerDTO=(ChatDTO)chatHandlerOis.readObject();
				shapeDTOList = (ArrayList<catchmind_ShapDTO>) chatHandlerOis.readObject();
				
				if(chatHandlerDTO.getCommand()==Info.JOIN) {
					chatNickName=chatHandlerDTO.getNickName();		
					chatHandlerDTO.setCommand(Info.SEND);
					chatHandlerDTO.setMessage("["+chatNickName+"]님이 입장하였습니다,  "+"매너채팅 하시길 바랍니다");
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}
				else if(chatHandlerDTO.getCommand()==Info.EXIT) {
					chatHandlerList.remove(this);
					chatHandlerDTO.setCommand(Info.EXIT);
					chatHandlerOos.writeObject(chatHandlerDTO);
					chatHandlerOos.writeObject(shapeDTOList);
					chatHandlerOos.flush();
					
					chatHandlerDTO.setCommand(Info.SEND);
					chatHandlerDTO.setMessage(chatNickName+"님이 퇴장하였습니다");
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
					
					chatHandlerOos.close();
					chatHandlerOis.close();
					chatHandlerSocket.close();
					break;
					
				}else if(chatHandlerDTO.getCommand()==Info.SEND) {
					chatHandlerDTO.setCommand(Info.SEND);
					chatHandlerDTO.setNickName(chatNickName);
					chatHandlerDTO.setMessage(chatHandlerDTO.getMessage());
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
					
				} else if(chatHandlerDTO.getCommand()==Info.WAIT) {
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}
				else if(chatHandlerDTO.getCommand()==Info.READY) {//★추가한 내용
					start = 0;
					ready = chatHandlerDTO.getReadyCount();
					chatHandlerDTO.setReadyCount(ready);
					chatHandlerDTO.setCommand(Info.READY);
					chatHandlerDTO.setMessage(ready+"명이 준비하였습니다");
					chatHandlerDTO.setReadyCount(ready);
					if(ready == 4) {
						chatHandlerDTO.setCommand(Info.READY);
						ready = 0;
					}
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}
				else if(chatHandlerDTO.getCommand()==Info.START) {//★추가한 내용
					start = chatHandlerDTO.getStartCount();
					chatHandlerDTO.setStartCount(start);
					chatHandlerDTO.setCommand(Info.START);
					ready = 0;
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}else if(chatHandlerDTO.getCommand() == Info.ANSWER) {//★추가한 내용
					chatHandlerDTO.setCommand(Info.ANSWER);
					chatHandlerDTO.setNickName(chatNickName);
					chatHandlerDTO.setMessage(chatHandlerDTO.getMessage());
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}
				
			}  catch (IOException e) {
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}			
		}//while종료
	}//run종료

	private void brodacast(ArrayList<catchmind_ShapDTO> shapeDTOList) {
		for(ChatHandler handler:chatHandlerList) {
			try {
				handler.chatHandlerOos.writeObject(shapeDTOList);
				handler.chatHandlerOos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}//for문 종료
		
		
	}

	private void broadcast(ChatDTO chatsendDTO) {//뿌려주는 메소드
		for(ChatHandler handler:chatHandlerList) {
			try {
				handler.chatHandlerOos.writeObject(chatsendDTO);
				handler.chatHandlerOos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}//for문 종료
	}
}
