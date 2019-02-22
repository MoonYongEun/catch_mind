package miniproject.catchmind;

import java.io.Serializable;

import miniproject.catchmind.Info;

public class waitingRoomUserDTO implements Serializable{
	private String name;
	private Info command;
	
	public Info getCommand() {
		return command;
	}

	public void setCommand(Info command) {
		this.command = command;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return name;
	}

	
}