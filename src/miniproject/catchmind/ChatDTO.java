package miniproject.catchmind;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/*
enum Info{
	JOIN,EXIT,SEND
}
*/
@SuppressWarnings("serial")
class ChatDTO implements Serializable{
	private int seq;
	private String nickName;
	private String message;
	private String id;
	private String password;
	private String email;

	private Info command;
	
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
	public Info getCommand() {
		return command;
	}
	public void setCommand(Info command) {
		this.command = command;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public void setId(String id) {
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setPassword(String password) {
		this.password=password;
	}
	public String getPassword() {
		return password;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public String getEmail() {
		return email;
	}
	
	@Override
	public String toString() {
		return nickName;//클래스명@참조값 대신 name 들어감
	}
	
	

}
