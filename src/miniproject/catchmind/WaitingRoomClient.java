package miniproject.catchmind;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
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
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import miniproject.membership.dao.MembershipDAO;

public class WaitingRoomClient extends JFrame implements ActionListener,MouseListener,ListSelectionListener,Runnable{
	private JLabel roomListL,userL,chattingL, idL, pointL;
	private JButton roomB,chattingB , myB, myIB;
	private static JTextArea chattingA;
	private JTextField chattingF, idF, pointF;
	DefaultListModel<waitingRoomRCreateDTO> roomModel;
	JList<waitingRoomRCreateDTO> roomList;
	private DefaultListModel<waitingRoomUserDTO> userModel;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	//ChatFrame에 넘길 user DTO 데이터 
	private String nickName;
	private int score;
	
	private ImageIcon iconImage;//★★여기변경
	private String iconImg = "cong.png";
	private int state=1;//여기추가
	private ImageIcon backImage;//
	private JScrollPane roomScrollPane;//
	private ImageIcon backCreateImage;//
	private JScrollPane createScrollPane;//
	
//-----------------방만들기 창 ---------------------------
	private JLabel roomNameL,passwordL,personL;
	private JTextField roomNameF,passwordF;
	private JButton roomCreateB,roomCancleB;
	private JComboBox<String> personCB;
//방만들때 필요한 정보 전송을 위한 ArrayList
	private ArrayList<waitingRoomUserDTO> UserData;
	private ArrayList<waitingRoomRCreateDTO> PersonCheck;
	
	
//-------------캔버스 보낼떄 필요한 ---------------------------
	private ArrayList<catchmind_ShapDTO> sendList;
	private ChatFrame chatframe;
	
	
	public WaitingRoomClient() {
		setLayout(null);
		//캔버스 보내기 리스트
		sendList = new ArrayList<catchmind_ShapDTO>();
		//유저데이터 보내기 리스트
		UserData = new ArrayList<waitingRoomUserDTO>();
		//방만들기시 인원수 체크
		PersonCheck = new ArrayList<waitingRoomRCreateDTO>();
		//방 목록
		roomListL = new JLabel("방 목록");
		roomModel = new DefaultListModel<waitingRoomRCreateDTO>();
		roomList = new JList<waitingRoomRCreateDTO>(roomModel);
		
		roomB = new JButton("방 만들기");

		//사용자 목록
		userL = new JLabel("사용자 목록 ");
		userModel = new DefaultListModel<waitingRoomUserDTO>();
		JList<waitingRoomUserDTO> userListL = new JList<waitingRoomUserDTO>(userModel);
	
		//대화창
		chattingL = new JLabel("대화창");
		chattingA = new JTextArea();
		chattingA.setEditable(false);
		JScrollPane scrollC = new JScrollPane(chattingA);
		scrollC.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chattingF = new JTextField(20);
		chattingB = new JButton("보내기");

		//내 정보
		idL = new JLabel("아이디");
		idF = new JTextField(20);
		idF.setEditable(false);
		pointL = new JLabel("점  수");
		pointF = new JTextField(20);
		pointF.setEditable(false);
		myB = new JButton("내 정보");
		myIB = new JButton(new ImageIcon("cong.png")); // 나중에 사진으로 대체 
		myIB.setEnabled(true);
		
		//setLatout 좌표
		int leftW = 50;//왼쪽 모서리 폭
		int rightW = 420;//왼쪽 부터 오른쪽 모서리 폭 

		roomListL.setBounds(leftW,20,50 ,20);
		roomList.setBounds(leftW, 40,350 ,230);
		roomB.setBounds(80+leftW,280,200,30);
		
		//채팅창 좌표
		chattingL.setBounds(leftW,340,50,20);
		scrollC.setBounds(leftW,370,350,200);
		chattingF.setBounds(leftW,570,260,30);
		chattingB.setBounds(leftW+260,570,90,30);
		
		//사용자목록 좌표
		userL.setBounds(rightW,20,80,20);
		userListL.setBounds(rightW,40,200,300);
		
		//내정보
		idL.setBounds(rightW+100,390,100,20);
		idF.setBounds(rightW+100,410,100,20);
		pointL.setBounds(rightW+100,450,100,20);
		pointF.setBounds(rightW+100,470,100,20);
		myB.setBounds(rightW,570,200,30);
		myIB.setBounds(rightW+10,380,80,150);// 나중에 사진으로 대체 
		
		Container con = this.getContentPane();
		//방
		con.add(roomListL); con.add(roomList);
		con.add(roomB);
		
		//대기자
		con.add(userL); con.add(userListL);
		
		//채팅방
		con.add(chattingL); con.add(scrollC);
		con.add(chattingF); con.add(chattingB);
		
		//내정보
		con.add(idL); 	 con.add(idF);
		con.add(pointL); con.add(pointF);
		con.add(myB); 	 con.add(myIB);
		
		
		
		//
		backImage = new ImageIcon("wait.jpg");
        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(backImage.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
            }     
        };
        background.setLayout(null);
        
        background.add(roomListL);
        background.add(roomList);
        background.add(roomB);
        background.add(userL);
        background.add(userListL);
        background.add(chattingL);
        background.add(scrollC);
        background.add(chattingF);
        background.add(chattingB);
        background.add(idL);
        background.add(idF);
        background.add(pointL);
        background.add(pointF);
        background.add(myB);
        background.add(myIB);
        
        roomScrollPane = new JScrollPane(background);
        setContentPane(roomScrollPane);
		//
		

		setTitle("CatchMind - Robby");
		setResizable(false);
		setBounds(750,300,700,700);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		myIB.addActionListener(new ActionListener()
		 {   
			 @Override
			 public void actionPerformed(ActionEvent e)
			 {
 // TODO Auto-generated method stub
				 if(e.getSource() == myIB)
				 {
  //상태에 따라 알맞게 버튼에 그림 넣어줌
					 if(state == 0)
					 {
						 iconImage = new ImageIcon("cong.png");
						 iconImg="cong.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state == 1)
					 {
						 iconImage = new ImageIcon("ag.png");
						 iconImg="ag.png";
						 myIB.setIcon(iconImage);      
					 }
					 else if(state == 2)
					 {
						 iconImage = new ImageIcon("cona.png");
						 iconImg="cona.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state == 3)
					 {
						 iconImage = new ImageIcon("dora.png");
						 iconImg="dora.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state == 4)
					 {
						 iconImage = new ImageIcon("bo.png");
						 iconImg="bo.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state == 5)
					 {
						 iconImage = new ImageIcon("je.png");
						 iconImg="je.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state ==6)
					 {
						 iconImage = new ImageIcon("ru.png");
						 iconImg="ru.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state ==7)
					 {
						 iconImage = new ImageIcon("jang.png");
						 iconImg="jang.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state ==8)
					 {
						 iconImage = new ImageIcon("pica.png");
						 iconImg="pica.png";
						 myIB.setIcon(iconImage);
					 }
						 
  
					 state++;
					 if(state > 8) state = 0;
				 }//if문 종료
 
			 }
		 });
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//oos = null;
				//ois = null;
				if(oos == null || ois == null ) ;
				try{
					WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
					waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
					waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
					ChatDTO chatDTO = new ChatDTO();
					GameUserDTO gameuserDTO = new GameUserDTO();
					
					waitingroomchattingDTO.setCommand(Info.EXIT);
					
					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO);
					oos.writeObject(sendList);
					oos.writeObject(gameuserDTO);
					oos.flush();
					//oos.close();
					//ois.close();
					//socket.close();
					
				}catch(IOException io){
					io.printStackTrace();
				}
				
				
			}
		});
	}
	
	public void event() {
		
		roomB.addActionListener(this);
		chattingB.addActionListener(this);
		chattingF.addActionListener(this);
		myB.addActionListener(this);
		roomList.addListSelectionListener(this);
		roomList.addMouseListener(this);
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == roomB) roomCreate();
		
		else if (e.getSource() == chattingB || 
				e.getSource()==chattingF) roomChatting();
			
		else if (e.getSource() == myB) myInfo();
	}
	
	//----------------방만들기 메소드 ---------------------
	private void roomCreate() {
		
		WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
		waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
		waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
		ChatDTO chatDTO=new ChatDTO();
		GameUserDTO gameuserDTO = new GameUserDTO();
		
	
		JFrame roomFrame = new JFrame();
		roomFrame.setLayout(null);
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
		
		roomFrame.add(roomNameL); roomFrame.add(roomNameF);
		roomFrame.add(passwordL); roomFrame.add(passwordF);
		roomFrame.add(personL);   roomFrame.add(personCB);
		roomFrame.add(roomCreateB);roomFrame.add(roomCancleB);
		//
		Container back =roomFrame.getContentPane();
		back.setBackground(new Color(230,255,255));
		//
		roomFrame.setTitle("방만들기");
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
				waitingroomchattingDTO.setCommand(Info.WAIT);
				waitingroomuserDTO.setCommand(Info.WAIT);
				waitingroomrcreateDTO.setRoomName(roomNameF.getText());
				waitingroomrcreateDTO.setRoomPass(passwordF.getText());
				waitingroomrcreateDTO.setPerson(personCB.getSelectedIndex());
				waitingroomrcreateDTO.setCommand(Info.CREATE);
				waitingroomrcreateDTO.setOwner("방장");
				
		
				
				if(roomNameF.getText()!=null) {
					try {
						if(waitingroomrcreateDTO.getPerson()== 0) {
							waitingroomrcreateDTO.setPerson(2);
						}else if(waitingroomrcreateDTO.getPerson() == 1) {
							waitingroomrcreateDTO.setPerson(3);
						}else if(waitingroomrcreateDTO.getPerson() == 2) {
							waitingroomrcreateDTO.setPerson(4);
						}
				
						
						//담아 보내기
						waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
						for(int i = 0; i<UserData.size();i++) {
							if(nickName.equals(UserData.get(i).getName())){
								waitingroomuserDTO_send.setName(UserData.get(i).getName());
								waitingroomuserDTO_send.setScore(UserData.get(i).getScore());
							}
						}
										
						ChatDTO chatDTO = new ChatDTO();
						chatDTO.setCommand(Info.WAIT);
						//chatDTO.setCommand(Info.JOIN);
						GameUserDTO gameuserDTO = new GameUserDTO();
						gameuserDTO.setCommand(Info.WAIT);
						//gameuserDTO.setCommand(Info.JOIN);

						
						oos.writeObject(waitingroomchattingDTO);
						oos.writeObject(waitingroomuserDTO);
						oos.writeObject(waitingroomrcreateDTO);
						oos.writeObject(chatDTO);
						oos.writeObject(sendList);
						oos.writeObject(gameuserDTO);
						oos.flush();
						
//						oos.close();
//						ois.close();
//						socket.close();
						
						
						chatframe = new ChatFrame(WaitingRoomClient.this);//방만들기 버튼
						//chatframe.service();
						chatframe.service(waitingroomrcreateDTO, waitingroomuserDTO_send);
							//	oos,ois);
						
						//ChatFrame chatframe = new ChatFrame(waitingroomrcreateDTO_send);
						
					} catch (IOException ie) {
						ie.printStackTrace();
					}
					
					

					setVisible(false);
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
	
	
	
	//------------------채팅 메소드 ------------------------------
	private void roomChatting() {				// 채팅 메세지 전송 
		
		String message = chattingF.getText();
		
		WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
		waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
		waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
		ChatDTO chatDTO = new ChatDTO();
		GameUserDTO gameuserDTO = new GameUserDTO();
		
		if(message.toLowerCase().equals("exit")) {
			waitingroomchattingDTO.setCommand(Info.EXIT);
			waitingroomuserDTO.setCommand(Info.EXIT);
			waitingroomrcreateDTO.setCommand(Info.EXIT);
			chatDTO.setCommand(Info.EXIT);
			gameuserDTO.setCommand(Info.EXIT);
			
		}else {
			waitingroomchattingDTO.setCommand(Info.SEND);
			waitingroomchattingDTO.setMessage(message);
			waitingroomuserDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			chatDTO.setCommand(Info.WAIT);
			gameuserDTO.setCommand(Info.WAIT);
			
		}
		
		try {
			oos.writeObject(waitingroomchattingDTO);
			oos.writeObject(waitingroomuserDTO);
			oos.writeObject(waitingroomrcreateDTO);
			oos.writeObject(chatDTO);
			oos.writeObject(sendList);
			oos.writeObject(gameuserDTO);
			oos.flush();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		chattingF.setText("");
		 
	}


	@Override
	public void run() {	//서버로 부터 받는쪽
		
		WaitingRoomChattingDTO waitingroomchattingDTO = null;
		waitingRoomUserDTO waitingroomuserDTO = null;
		waitingRoomRCreateDTO waitingroomrcreateDTO = null;
		ChatDTO chatHandlerDTO = null;
		GameUserDTO gameuserDTO = null;
		
		while(true) {
			
			try {
				waitingroomchattingDTO = (WaitingRoomChattingDTO)ois.readObject();
				waitingroomuserDTO = (waitingRoomUserDTO)ois.readObject();
				waitingroomrcreateDTO = (waitingRoomRCreateDTO)ois.readObject();
				chatHandlerDTO = (ChatDTO)ois.readObject();
				sendList = (ArrayList<catchmind_ShapDTO>) ois.readObject();
				gameuserDTO = (GameUserDTO)ois.readObject();
		
				if(waitingroomchattingDTO.getCommand()==Info.EXIT){
					
					
					
					//userModel.removeElement(waitingroomuserDTO);
					//roomModel.removeElement(waitingroomrcreateDTO);
					
					oos.close();
					ois.close();
					socket.close();
					//chatframe.
					System.exit(0);
					while(true) {
						if(socket.isClosed()) {
							System.out.println("닫힘!");					
						}
					}
	
					
				}else if(waitingroomchattingDTO.getCommand() == Info.SEND) {
					chattingA.append(waitingroomchattingDTO.getMessage()+"\n");
					int pos = chattingA.getText().length();// 스크롤바가 내려갈때 따라가게끔 해주는역활
					chattingA.setCaretPosition(pos);
				}

				
				if(waitingroomuserDTO.getCommand() == Info.JOIN) {
					UserData.add(waitingroomuserDTO); // 유저 데이터 입력
					userModel.addElement(waitingroomuserDTO);
					
				}else if (waitingroomuserDTO.getCommand() == Info.WAIT) {
				
				}
				
				if(waitingroomrcreateDTO.getCommand() == Info.CREATE) {
					
					if(waitingroomrcreateDTO.getOwner().equals("방장"))
						roomModel.addElement(waitingroomrcreateDTO);//방장이 만들시만 방목록에 추가
					
					PersonCheck.add(waitingroomrcreateDTO);// 방 인원수 체크

					
				}
		
				if(gameuserDTO.getCommand() == Info.EXIT) {
					
					System.out.println("gameuser = "+gameuserDTO.getName());
					String user = gameuserDTO.getName();
					MembershipDAO membershipDAO = MembershipDAO.getInstance();
					//String user = membershipDAO.getName(userID)
					
					int newScore = membershipDAO.getScore(user);
					//int newScore = gameuserDTO.getPoint();
					idF.setText(user);
					pointF.setText(newScore+"");
					
					//if(nickName.equals(gameuserDTO.getName()))setVisible(true);// WaitingRoomClient 464에 추가 
				
				}
				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void service(ChatDTO chatDTO) {
		System.out.println("돌아오자");
	}
	
	
	
	
	//public void service(IdNameScoreDTO idnamescoreDTO) {
	public void service( ) {	
		//nickName = idnamescoreDTO.getId();
		//id = getName();
		//score = idnamescoreDTO.getScore();
		nickName = JOptionPane.showInputDialog("아이디를 입력하세요");
		String serverIP = JOptionPane.showInputDialog("서버IP를 입력하세요","192.168.51.97");
		//String serverIP ="192.168.51.97";
																		
		if(serverIP==null || serverIP.length()==0){
			System.out.println("서버 IP가 입력되지 않았습니다");
			System.exit(0);
		}
		
		try {
			
			socket = new Socket(serverIP, 9500);			//서버 입력 : 아직 미입력 
			oos = new ObjectOutputStream(socket.getOutputStream()); 
			ois = new ObjectInputStream(socket.getInputStream());
			
			//채팅 DTO
			WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
			waitingroomchattingDTO.setCommand(Info.JOIN);
			waitingroomchattingDTO.setNickName(nickName);	// 로그인시 닉네임 받아오기 : 아직 미입력
			
			//대기실 DTO
			waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
			waitingroomuserDTO.setCommand(Info.JOIN);
			waitingroomuserDTO.setName(nickName);
			
			//waitingroomuserDTO.setId();
			//waitingroomuserDTO.setScore();
			
			//방만들기 DTO
			waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			
			//게임 DTO 생성
			ChatDTO chatDTO=new ChatDTO();
			chatDTO.setCommand(Info.WAIT);
			
			//게임 유저 리스트 DTO
			GameUserDTO gameuserDTO = new GameUserDTO();
			gameuserDTO.setCommand(Info.WAIT);
			
			//idF.setText(idnamescoreDTO.getId());
			//pointF.setText(idnamescoreDTO.getScore()+"");
			
			//5개 서버로 전송 
			oos.writeObject(waitingroomchattingDTO);
			oos.writeObject(waitingroomuserDTO);
			oos.writeObject(waitingroomrcreateDTO);
			oos.writeObject(chatDTO);
			oos.writeObject(sendList);
			oos.writeObject(gameuserDTO);
			oos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		//roomList.getSelectedIndex();
		//System.out.println(roomList.getSelectedIndex());
		//roomList.getSelectedValue();
		//System.out.println(roomList.getSelectedValue());
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) { 		//마우스로 방들어가기 
		if(arg0.getComponent() == roomList) {
			int answer = 0;
			
			if(arg0.getClickCount()==2) {
				
				if(roomList.getSelectedIndex()>= 0) {
					answer = JOptionPane.showConfirmDialog( this, "방에 입장 하시겠습니까?","방만들기",JOptionPane.YES_NO_CANCEL_OPTION); //실행될꺼 , 메세지 
					
					waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
					waitingroomrcreateDTO_send.setCommand(Info.CREATE);
					waitingroomrcreateDTO_send = roomList.getSelectedValue();
	
					if(answer==JOptionPane.YES_OPTION){
						if( PersonCheck.get(waitingroomrcreateDTO_send.getRoomNumber()-1).getRoomName().equals(waitingroomrcreateDTO_send.getRoomName()) &&
								waitingroomrcreateDTO_send.getPerson() > PersonCheck.size() ) {//방제목 체크 , 인원수 체크

							try {
								int roomListNumber = roomList.getSelectedIndex();
								
								WaitingRoomChattingDTO waitingroomchattingDTO_send= new WaitingRoomChattingDTO();
								waitingroomchattingDTO_send.setCommand(Info.WAIT);
								
								waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
								waitingroomuserDTO_send.setCommand(Info.WAIT);
								for(int i = 0; i<UserData.size();i++) {
									if(nickName.equals(UserData.get(i).getName())){
										waitingroomuserDTO_send.setName(UserData.get(i).getName());
										waitingroomuserDTO_send.setScore(UserData.get(i).getScore());
									}
								}
								
								// 방 DTO 를  복사
							
								waitingroomrcreateDTO_send.setOwner("유저");
								waitingroomrcreateDTO_send.setRoomNumber(roomListNumber); //방 번호 입력
								
								//System.out.println(roomList.getSelectedIndex());
								
		
								
								ChatDTO chatDTO = new ChatDTO();
								chatDTO.setCommand(Info.WAIT);
								GameUserDTO gameuserDTO = new GameUserDTO();
								gameuserDTO.setCommand(Info.WAIT);
								
								oos.writeObject(waitingroomchattingDTO_send);
								oos.writeObject(waitingroomuserDTO_send);
								oos.writeObject(waitingroomrcreateDTO_send);
								oos.writeObject(chatDTO);
								oos.writeObject(sendList);
								oos.writeObject(gameuserDTO);
								oos.flush();
								
								
								//oos.close();
								//ois.close();
								//socket.close();
								chatframe = new ChatFrame(); //마우스 클릭
								//chatframe.service();
								chatframe.service( waitingroomrcreateDTO_send, waitingroomuserDTO_send);
								
								setVisible(false);
								
								
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else {
							 JOptionPane.showMessageDialog( this, "정원을 초과했습니다");
						}
						

					}//YES_OPTION
					
				}else if(answer==JOptionPane.NO_OPTION) {
					
				}

			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	//------------------ 내정보 ----------------------------------
	public void myInfo() {
		waitingRoomMyInfo waitingroommyinfo = new waitingRoomMyInfo();
		waitingroommyinfo.myinfoC(iconImg, nickName);
	}
	//----------------------------------------------------
	
	
	
	public static void main(String []args) {
		WaitingRoomClient waitingroomclient = new WaitingRoomClient();
		waitingroomclient.event();
		waitingroomclient.service();
	}
	 

}

