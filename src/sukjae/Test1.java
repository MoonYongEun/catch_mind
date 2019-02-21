package sukjae;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Test1 extends JFrame{
	private JLabel roomNameL,passwordL,personL;
	private JTextField roomNameF,passwordF;
	private JButton roomCreateB,roomCancleB;
	private JComboBox<String> personCB;
	private String roomName;
	private String passWord;
	private int personNumber;
	private int roomkey;
	

	public void roomC(RoomDTO roomDTO) {
		setLayout(null);
		roomkey=0;
		String[] number= {"2","3","4"};
		
		roomNameL = new JLabel("방 제목");
		roomNameF = new JTextField(30);
		
		passwordL = new JLabel("비밀번호");
		passwordF = new JTextField(10);
		
		personL = new JLabel("게임인원");
		personCB = new JComboBox<String>(number);
		personCB.setSelectedItem(0);
		
		roomCreateB = new JButton(" 만들기 ");
		roomCancleB = new JButton(" 취  소 ");
		
		roomNameL.setBounds(50,30,70,30);
		roomNameF.setBounds(110,30,150,30);
		passwordL.setBounds(50,90,70,30);
		passwordF.setBounds(110,90,70,30);
		personL.setBounds(50,150,70,30);
		personCB.setBounds(110,150,50,30);
		roomCreateB.setBounds(30,210 , 100, 40);
		roomCancleB.setBounds(140,210 , 100, 40);
		
		add(roomNameL); add(roomNameF);
		add(passwordL); add(passwordF);
		add(personL);   add(personCB);
		add(roomCreateB);add(roomCancleB);
		
		setTitle("방만들기");
		setBounds(750,300,300,300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		roomCreateB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				roomName = roomNameF.getText();
				passWord = passwordF.getText();
				personNumber = personCB.getSelectedIndex();
				
				if(roomNameF.getText()!=null)dispose();
				roomkey=1;
			}
		});
		roomCancleB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void	windowClosing(WindowEvent e){
				dispose();
			}
		});
		
	}






	public int getRoomkey() {
		return roomkey;
	}






	public void setRoomkey(int roomkey) {
		this.roomkey = roomkey;
	}






	public void setRoomCreateB(JButton roomCreateB) {
		this.roomCreateB = roomCreateB;
	}



	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public int getPersonNumber() {
		return personNumber;
	}


	public void setPersonNumber(int personNumber) {
		this.personNumber = personNumber;
	}


}
