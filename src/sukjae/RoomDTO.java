package sukjae;

import java.io.Serializable;

public class RoomDTO implements Serializable{
		private String roomName;
		private String roomPass;
		private int person;//0 ->2명  // 1->3명 //2->4명 
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
			if(person== 0) {
				person = 2;
			}else if(person == 1) {
				person = 3;
			}else if(person == 2) {
				person = 4;
			}
			this.person = person;
		}
		
		public String toString() {
			return "["+roomName+"]"+"인원수 : "+person;
		}
		
}
