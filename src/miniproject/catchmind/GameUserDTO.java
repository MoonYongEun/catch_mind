package miniproject.catchmind;

import java.io.Serializable;

public class GameUserDTO implements Serializable {
	private String name;
	private int point;
	private Info command;
	
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Info getCommand() {
		return command;
	}
	public void setCommand(Info command) {
		this.command = command;
	}
	
	public String toString() {
		return "["+name+"]"+"Á¡¼ö :"+point;
		
	}
}
