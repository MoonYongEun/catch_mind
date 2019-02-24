package miniproject.catchmind;

import java.io.Serializable;

enum Info{
	JOIN , EXIT,SEND , CREATE, WAIT ,DELETE
}

public class WaitingRoomChattingDTO implements Serializable{
	//private int seq;
	private String nickName;
	private String message;
	private Info command;

	
	
	
	/*
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	*/
	
	
	public Info getCommand() {
		return command;
	}
	public void setCommand(Info command) {
		this.command = command;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String toString() {
		return nickName;
	}

}
