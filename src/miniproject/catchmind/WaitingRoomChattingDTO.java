package miniproject.catchmind;

import java.io.Serializable;

enum Info{
	JOIN , EXIT,SEND , CREATE, WAIT ,DELETE
}

public class WaitingRoomChattingDTO implements Serializable{
	//private int seq;
	private String nickName;
	private String message;
	private Info command_A;
	private String command_B;
	private Info command_C;
	private Info command_E;
	
	
	public String getCommand_B() {
		return command_B;
	}
	public void setCommand_B(String command_B) {
		this.command_B = command_B;
	}


	
	/*
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	*/
	
	public Info getCommand_E() {
		return command_E;
	}
	public void setCommand_E(Info command_E) {
		this.command_E = command_E;
	}
	public Info getCommand_A() {
		return command_A;
	}
	public void setCommand_A(Info command_A) {
		this.command_A = command_A;
	}

	public Info getCommand_C() {
		return command_C;
	}
	public void setCommand_C(Info command_C) {
		this.command_C = command_C;
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
