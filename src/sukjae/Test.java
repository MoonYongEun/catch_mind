package sukjae;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class Test extends JFrame implements ActionListener,Runnable{
	private JLabel roomListL,userL,chattingL, idL, pointL;
	private JButton roomB,chattingB , myB, myIB;
	private static JTextArea chattingA;
	private JTextField chattingF, idF, pointF;
	DefaultListModel<RoomDTO> roomModel;
	private DefaultListModel<UserDTO> userModel;
	private String message;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int sw;
	
	private JLabel roomNameL,passwordL,personL;
	private JTextField roomNameF,passwordF;
	private JButton roomCreateB,roomCancleB;
	private JComboBox<String> personCB;

	
	
	public Test() {
		setLayout(null);

		//�� ���
		roomListL = new JLabel("�� ���");
		roomModel = new DefaultListModel<RoomDTO>();
		JList<RoomDTO> roomList = new JList<RoomDTO>(roomModel);
		//roomModel.addElement("TestRoom");
		roomB = new JButton("�� �����");

		//����� ���
		userL = new JLabel("����� ��� ");
		userModel = new DefaultListModel<UserDTO>();
		JList<UserDTO> userListL = new JList<UserDTO>(userModel);
	
		//JScrollPane scrollU = new JScrollPane(userListL);
		//scrollU.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		//��ȭâ
		chattingL = new JLabel("��ȭâ");
		chattingA = new JTextArea();
		chattingA.setEditable(false);
		JScrollPane scrollC = new JScrollPane(chattingA);
		scrollC.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chattingF = new JTextField(20);
		chattingB = new JButton("������");

		//�� ����
		idL = new JLabel("���̵�");
		idF = new JTextField(20);
		idF.setEditable(false);
		pointL = new JLabel("��  ��");
		pointF = new JTextField(20);
		pointF.setEditable(false);
		myB = new JButton("�� ����");
		myIB = new JButton(new ImageIcon("red.png")); // ���߿� �������� ��ü 
		myIB.setEnabled(false);
		
		//setLatout ��ǥ
		int leftW = 50;//���� �𼭸� ��
		int rightW = 420;//���� ���� ������ �𼭸� �� 

		roomListL.setBounds(leftW,20,50 ,20);
		roomList.setBounds(leftW, 40,350 ,230);
		roomB.setBounds(80+leftW,280,200,30);
		
		//ä��â ��ǥ
		chattingL.setBounds(leftW,340,50,20);
		scrollC.setBounds(leftW,370,350,200);
		chattingF.setBounds(leftW,570,260,30);
		chattingB.setBounds(leftW+260,570,90,30);
		
		//����ڸ�� ��ǥ
		userL.setBounds(rightW,20,80,20);
		userListL.setBounds(rightW,40,200,300);
		
		//������
		idL.setBounds(rightW+100,390,100,20);
		idF.setBounds(rightW+100,410,100,20);
		pointL.setBounds(rightW+100,450,100,20);
		pointF.setBounds(rightW+100,470,100,20);
		myB.setBounds(rightW,570,200,30);
		myIB.setBounds(rightW+10,380,80,150);// ���߿� �������� ��ü 
		
		Container con = this.getContentPane();
		//��
		con.add(roomListL); con.add(roomList);
		con.add(roomB);
		
		//�����
		con.add(userL); con.add(userListL);
		
		//ä�ù�
		con.add(chattingL); con.add(scrollC);
		con.add(chattingF); con.add(chattingB);
		
		//������
		con.add(idL); 	 con.add(idF);
		con.add(pointL); con.add(pointF);
		con.add(myB); 	 con.add(myIB);

		setTitle("CatchMind - Robby");
		setResizable(false);
		setBounds(750,300,700,700);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(oos == null || ois == null )System.exit(0);
				try{
					TestDTO testDTO = new TestDTO();
					UserDTO userDTO = new UserDTO();
					RoomDTO roomDTO = new RoomDTO();
					
					testDTO.setCommand(Info.EXIT);
					userDTO.setCommand(Info.EXIT);
					roomDTO.setCommand(Info.EXIT);
					
					oos.writeObject(testDTO);
					oos.writeObject(userDTO);
					oos.writeObject(roomDTO);
					oos.flush();
					
				}catch(IOException io){
					io.printStackTrace();
				}
				
			}
		});
	}
	
	private void event() {
		
		roomB.addActionListener(this);
		chattingB.addActionListener(this);
		chattingF.addActionListener(this);
		myB.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == roomB) roomCreate();
		
		else if (e.getSource() == chattingB || 
				e.getSource()==chattingF) roomChatting();
			
		else if (e.getSource() == myB) myInfo();
	}
	
	//----------------�游��� �޼ҵ� ---------------------
	private void roomCreate() {
		
		TestDTO testDTO = new TestDTO();
		UserDTO userDTO = new UserDTO();
		RoomDTO roomDTO = new RoomDTO();
		
		sw = 0;
	
		JFrame roomFrame = new JFrame();
		roomFrame.setLayout(null);
		String[] number= {"2","3","4"};
		
		roomNameL = new JLabel("�� ����");
		roomNameF = new JTextField(30);
		
		passwordL = new JLabel("��й�ȣ");
		passwordF = new JTextField(10);
		
		personL = new JLabel("�����ο�");
		personCB = new JComboBox<String>(number);
		personCB.setSelectedItem(0);
		
		roomCreateB = new JButton(" ����� ");
		roomCancleB = new JButton(" ��  �� ");
		
		roomNameL.setBounds(50,30,70,30);
		roomNameF.setBounds(110,30,150,30);
		passwordL.setBounds(50,90,70,30);
		passwordF.setBounds(110,90,70,30);
		personL.setBounds(50,150,70,30);
		personCB.setBounds(110,150,50,30);
		roomCreateB.setBounds(30,210 , 100, 40);
		roomCancleB.setBounds(140,210 , 100, 40);
		
		roomFrame.add(roomNameL); roomFrame.add(roomNameF);
		roomFrame.add(passwordL); roomFrame.add(passwordF);
		roomFrame.add(personL);   roomFrame.add(personCB);
		roomFrame.add(roomCreateB);roomFrame.add(roomCancleB);
		
		roomFrame.setTitle("�游���");
		roomFrame.setBounds(750,300,300,300);
		roomFrame.setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void	windowClosing(WindowEvent e){
				roomFrame.dispose();
			}
		});
		roomCreateB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				roomDTO.setRoomName(roomNameF.getText());
				roomDTO.setRoomPass(passwordF.getText());
				roomDTO.setPerson(personCB.getSelectedIndex());
				roomDTO.setCommand(Info.CREATE);
				
				System.out.println("roomDTO : "+roomDTO);
				System.out.println("roomDTO.NAME : "+roomDTO.getRoomName());
				
				if(roomNameF.getText()!=null) {
					try {
						oos.writeObject(testDTO);
						oos.writeObject(userDTO);
						oos.writeObject(roomDTO);
						oos.flush();
						
					} catch (IOException ie) {
						ie.printStackTrace();
					}
					roomFrame.dispose();
				}
			}
		});
		roomCancleB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				roomFrame.dispose();
			}
		});

	}
	
	

	//-----------------------------------------------
	//����� ����Ʈ
		
	
	//------------------ä�� �޼ҵ� ------------------------------
	private void roomChatting() {				// ä�� �޼��� ���� 
		
		String message = chattingF.getText();
		
		TestDTO testDTO = new TestDTO();
		UserDTO userDTO = new UserDTO();
		RoomDTO roomDTO = new RoomDTO();
		
		if(message.toLowerCase().equals("exit")) {
			testDTO.setCommand(Info.EXIT);
			userDTO.setCommand(Info.EXIT);
		}else {
			testDTO.setCommand(Info.SEND);
			testDTO.setMessage(message);
			userDTO.setCommand(Info.SEND);
		}
		
		try {
			oos.writeObject(testDTO);
			oos.writeObject(userDTO);
			oos.writeObject(roomDTO);
			oos.flush();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		chattingF.setText("");
		 
	}

	
	@Override
	public void run() {	//������ ���� �޴���
		TestDTO testDTO = null;
		UserDTO userDTO = null;
		RoomDTO roomDTO = null;
		
		while(true) {
			try {
				testDTO = (TestDTO)ois.readObject();
				userDTO = (UserDTO)ois.readObject();
				roomDTO = (RoomDTO)ois.readObject();
			
				
					
				if(testDTO.getCommand()==Info.EXIT && userDTO.getCommand()==Info.EXIT && roomDTO.getCommand()==Info.EXIT) {
					oos.close();
					ois.close();
					socket.close();
					System.exit(0);
				
				}else if(testDTO.getCommand() == Info.SEND) {
					chattingA.append(testDTO.getMessage()+"\n");
					
					int pos = chattingA.getText().length();// ��ũ�ѹٰ� �������� ���󰡰Բ� ���ִ¿�Ȱ
					chattingA.setCaretPosition(pos);

				}
						
				if(userDTO.getCommand() == Info.JOIN) {
					userModel.addElement(userDTO);
				}
			
				if(userDTO.getCommand() == Info.SEND) {
				
				}
				
				
				if(roomDTO.getCommand() == Info.CREATE) {
					if(roomDTO.getPerson()== 0) {
						roomDTO.setPerson(2);
					}else if(roomDTO.getPerson() == 1) {
						roomDTO.setPerson(3);
					}else if(roomDTO.getPerson() == 2) {
						roomDTO.setPerson(4);
					}
					roomModel.addElement(roomDTO);
				}
				
				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void service() {
		String nickName = "���";
		String serverIP = JOptionPane.showInputDialog("����IP�� �Է��ϼ���","192.168.");
																		//��� ���� �޾� ����   : ���� ���Է�
		if(serverIP==null || serverIP.length()==0){
			System.out.println("���� IP�� �Էµ��� �ʾҽ��ϴ�");
			System.exit(0);
		}
		
		try {
			socket = new Socket(serverIP, 9000);			//���� �Է� : ���� ���Է� 
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			//ä�� DTO
			TestDTO testDTO = new TestDTO();
			testDTO.setCommand(Info.JOIN);
			testDTO.setNickName(nickName);	// �α��ν� �г��� �޾ƿ��� : ���� ���Է�
			
			//���� DTO
			UserDTO userDTO = new UserDTO();
			userDTO.setCommand(Info.JOIN);
			userDTO.setName(nickName);
			
			//�游��� DTO
			RoomDTO roomDTO = new RoomDTO();
			
			oos.writeObject(testDTO);
			oos.writeObject(userDTO);
			oos.writeObject(roomDTO);
			oos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread thread = new Thread(this);
		thread.start();
	}
	//------------------ ������ ----------------------------------
	private void myInfo() {
		myInfo myinfo = new myInfo();
		myinfo.myinfoC();
	}
	
	//---------------��  ��---------------------------------------

	public static void main(String[] args) {
		Test test = new Test();
		test.event();
		test.service();
	
	}
}