package miniproject.catchmind;

import miniproject.catchmind.Info;

import java.io.Serializable;

public class waitingRoomRCreateDTO implements Serializable{
		private String roomName;
		private String roomPass;
		private int person;//0 ->2��  // 1->3�� //2->4�� 
		private Info command;
		private int sw;
		
		public int getSw() {
			return sw;
		}
		public void setSw(int sw) {
			this.sw = sw;
		}
		public Info getCommand() {
			return command;
		}
		public void setCommand(Info command) {
			this.command = command;
		}
		public String getRoomName() {
			return roomName;
		}
		public void setRoomName(String roomName) {
			this.roomName = roomName;
		}
		public String getRoomPass() {
			return roomPass;
		}
		public void setRoomPass(String roomPass) {
			this.roomPass = roomPass;
		}
		public int getPerson() {
			return person;
		}
		public void setPerson(int person) {
			this.person = person;
		}
		
		public String toString() {
			return "["+roomName+"]"+" �ο��� : "+person;
		}
		
}