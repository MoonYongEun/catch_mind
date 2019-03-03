package miniproject.catchmind;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import miniproject.membership.dao.MembershipDAO;




@SuppressWarnings("serial")
class ChatFrame extends JFrame implements ActionListener,Runnable{
	private JTextField chatSend, answerT; //입력창, 정답창
	private JTextArea chatPrint; //메세지 출력창
	private JList<GameUserDTO> playerList; //참가자 목록창
	private JButton exitB,sendB,readyB,startB, answerB, clearB;//나가기버튼,전송버튼,준비버튼,시작버튼, 지우개버튼
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket chatSocket;
	private DefaultListModel<GameUserDTO> chatModel;
	private String chatNickName;
	private int point;
	private int readyCount=0; //추가한거
	private int startCount=0; //★추가한 내용
	private boolean timestop=true;//추가한거
	private static int ii;//추가한거
	private String[] Quiz = {"고양이", "개", "태양", "달", "컴퓨터"};//★추가한 내용
	int[] list = new int[5];//★추가한 내용
	private JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL,0,120); //추가한거
	private int score;
	//
	private ImageIcon chatImage;//
	private JScrollPane chatScrollPane;//
	//
	private int x1, y1, x2, y2; //좌표
	private JButton[] btn; //색깔 버튼
	private JRadioButton thinB, thickB; //선 굵기
	private catchmind_Canvas can = new catchmind_Canvas(this); //캔버스
	private ArrayList<catchmind_ShapDTO> sendList; //서버로 보내는 list
	private ArrayList<catchmind_ShapDTO> serverInfoList; //서버에서 받아온 list	
	private ArrayList<catchmind_ShapDTO> shapeDTOList;
	private ArrayList<catchmind_ShapDTO> cloneList;
	private String answer;
	private WaitingRoomClient waitingRoomClient;
	private int colorNum; //색깔 기준
	
	//------------방정보 가져오는거 등록---------------
	private ArrayList<waitingRoomRCreateDTO> RoomData;
	private String roomName;
	private String roomPass;
	private String owner;
	private int person;
	private int roomNumber;

	
	public ChatFrame(WaitingRoomClient waitingRoomClient) {
		this();
		this.waitingRoomClient = waitingRoomClient;
		
	}
	
	public ChatFrame() {
			//ObjectOutputStream oos, ObjectInputStream ois){
		super("이것은 그림인가 낙서인가 이때까지 이런 게임은 없었다.");
		//this.chatSocket=chatSoket;
		//this.oos = oos;
		//this.ois = ois;
		
		//System.out.println("chatFrame"+waitingroomuserDTO.getName());
		
		
		

		
		setLayout(null);
		
		chatSend = new JTextField();
		answerT = new JTextField(); //정답TextField
		chatPrint= new JTextArea();
		chatModel = new DefaultListModel<GameUserDTO>();
		playerList = new JList<GameUserDTO>(chatModel);
		exitB=new JButton("나가기");
		sendB=new JButton("전송");
		readyB=new JButton("준비");
		startB=new JButton("시작");
		answerB=new JButton("정답");
		clearB = new JButton("지우기");
		
		chatPrint.setEditable(false);
		startB.setEnabled(false);//여기 나중에 flase로 변경해줘야함
		
		JScrollPane scroll = new JScrollPane(chatPrint);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add("Center",scroll);//
		
		chatSend.setBounds(640,585,300,30);
		answerT.setBounds(50, 580, 470, 30);
		scroll.setBounds(640,360,400,220);
		playerList.setBounds(640,50,395,300);
		exitB.setBounds(1050,50,100,30);
		sendB.setBounds(960,585,80,30);
		readyB.setBounds(635,650,200,50);
		startB.setBounds(850,650,200,50);
		answerB.setBounds(530, 580, 70, 30);
		clearB.setBounds(220, 710, 110, 50);
		bar.setBounds(1050, 100, 100, 30);
		bar.setVisible(false);
		
		btn = new JButton[6];
		String[] title = {"검정", "빨강", "초록", "파랑", "노랑", "분홍"};
		Color[] color = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
		for(int i=0; i<title.length; i++) {
			btn[i] = new JButton(title[i]);
			btn[i].setBackground(color[i]);
		}
		btn[0].setBounds(50, 620, 80, 80);
		btn[1].setBounds(155, 620, 80, 80);
		btn[2].setBounds(245, 620, 80, 80);
		btn[3].setBounds(335, 620, 80, 80);
		btn[4].setBounds(425, 620, 80, 80);
		btn[5].setBounds(515, 620, 80, 80);
		
		//선 굵기
		thinB = new JRadioButton("얇은", true);
		thickB = new JRadioButton("굵은");
		thinB.setBounds(50, 700, 60, 30);
		thickB.setBounds(150, 700, 60, 30);
		thinB.setBackground(new Color(230,255,255));
		thickB.setBackground(new Color(230,255,255));
		
		//JRadioButton 그룹
		ButtonGroup group = new ButtonGroup();
		group.add(thickB); group.add(thinB);
		
		//캔버스
		can.setBounds(50, 50, 550, 525);
		can.setEnabled(false);
		
		//리스트
		sendList = new ArrayList<catchmind_ShapDTO>();
		serverInfoList = new ArrayList<catchmind_ShapDTO>();
		RoomData = new ArrayList<waitingRoomRCreateDTO>();
		
		bar.setStringPainted(true);//추가한거
		bar.setString("0초");//추가한거
		
		//
		chatImage = new ImageIcon("game.jpg");
        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(chatImage.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
            }     
        };
        background.setLayout(null);
       
        
        background.add(answerB);
        background.add(clearB);
        background.add(answerT);
        
        background.add(can);
        background.add(thickB);
        background.add(thinB);
        
        background.add(chatSend);
        background.add("Center", scroll);
        background.add(playerList);
        background.add(exitB);
        background.add(sendB);
        background.add(readyB);
        background.add(startB);
        background.add(bar);
        for(int i=0; i<title.length; i++) {
        	background.add(btn[i]);
		}
        chatScrollPane = new JScrollPane(background);
        setContentPane(chatScrollPane);
        setResizable(false);
		//
        
		setBounds(700,100, 1200, 800);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		//버튼 이벤트
		for(int i=0; i<title.length; i++) btn[i].addActionListener(this);
		
		for(int i=0; i<5; i++) {
			list[i] = (int)(Math.random()*5);
			for(int j=0; j<i; j++) {
				if(list[i] == list[j]) {
					i--;
					break;
				}
			}
		}
		clearB.addActionListener(this);
		answerB.addActionListener(this); //★추가한 내용
		
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				MembershipDAO membershipDAO = MembershipDAO.getInstance();
				membershipDAO.scoreUpdate(chatNickName, score);
				try {
					if(oos==null||ois==null)dispose();
						WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
						waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
						waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
						waitingroomrcreateDTO.setRoomName(roomName);
						ChatDTO chatdto=new ChatDTO();
						chatdto.setCommand(Info.EXIT);
						GameUserDTO gameuserDTO = new GameUserDTO();
						
						oos.writeObject(waitingroomchattingDTO);
						oos.writeObject(waitingroomuserDTO);
						oos.writeObject(waitingroomrcreateDTO);
						oos.writeObject(chatdto);
						oos.writeObject(sendList);
						oos.writeObject(gameuserDTO);
						oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		can.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				x1 =e.getX();y1 =e.getY();
			}
			@Override
			public void mouseReleased(MouseEvent e){//       ===================   마우스 릴리즈 ===========================
				x2 = e.getX();	y2 = e.getY();

				catchmind_ShapDTO dto = new catchmind_ShapDTO();
				dto.setX1(x1);	dto.setY1(y1);	dto.setX2(x2);	dto.setY2(y2);
				
				dto.setColorNum(colorNum);
				
				if(thinB.isSelected()) dto.setShape(Shape.LINE);
				else if(thickB.isSelected()) dto.setShape(Shape.RECT);				
				sendList.add(dto);
				ArrayList<catchmind_ShapDTO> cloneList = (ArrayList<catchmind_ShapDTO>) sendList.clone();
				
				try {
					WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
					waitingroomchattingDTO.setCommand(Info.WAIT);
					waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
					waitingroomuserDTO.setCommand(Info.WAIT);
					waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
					waitingroomrcreateDTO.setCommand(Info.WAIT);
					waitingroomrcreateDTO.setRoomName(roomName);
					ChatDTO chatDTO_send = new ChatDTO();
					chatDTO_send.setCommand(Info.WAIT);
					GameUserDTO gameuserDTO = new GameUserDTO();
					gameuserDTO.setCommand(Info.WAIT);
					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO_send);
					oos.writeObject(cloneList);
					oos.writeObject(gameuserDTO);
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}
		});
		
		can.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				//System.out.print("drag    ");
				x2 = e.getX();	y2 = e.getY();	can.repaint();
				
				//연필
				catchmind_ShapDTO dto = new catchmind_ShapDTO();
				dto.setX1(x1);	dto.setY1(y1);	dto.setX2(x2);	dto.setY2(y2);				
				dto.setColorNum(colorNum);				
				if(thinB.isSelected()) 	dto.setShape(Shape.LINE);
				else if(thickB.isSelected()) dto.setShape(Shape.RECT);
				sendList.add(dto);
				cloneList = (ArrayList<catchmind_ShapDTO>) sendList.clone();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
				
				try {
					WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
					waitingroomchattingDTO.setCommand(Info.WAIT);
					waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
					waitingroomuserDTO.setCommand(Info.WAIT);
					waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
					waitingroomrcreateDTO.setCommand(Info.WAIT);
					waitingroomrcreateDTO.setRoomName(roomName);
					ChatDTO chatDTO_send = new ChatDTO();
					chatDTO_send.setCommand(Info.WAIT);
					GameUserDTO gameuserDTO = new GameUserDTO();
					gameuserDTO.setCommand(Info.WAIT);
					
					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO_send);
					oos.writeObject(cloneList);
					oos.writeObject(gameuserDTO);
					oos.flush();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
				
				x1 = x2;
				y1 = y2;
				

			}
		});
		
		exitB.addActionListener(this);
		sendB.addActionListener(this);
		readyB.addActionListener(this);
		startB.addActionListener(this);
		chatSend.addActionListener(this);//엔터키로 전송가능
		
	}//생성자 종료

	@Override
	public void actionPerformed(ActionEvent e) {
		String chatMsg=chatSend.getText();
		
		WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
		waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
		waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
		ChatDTO chatDTO =new ChatDTO();
		GameUserDTO gameuserDTO = new GameUserDTO();
		
		if(e.getSource()==chatSend || e.getSource()==sendB) {
			waitingroomchattingDTO.setCommand(Info.WAIT);
			waitingroomuserDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setRoomName(roomName);
			chatDTO.setNickName(chatNickName);
			chatDTO.setCommand(Info.SEND);
			chatDTO.setMessage(chatMsg);
			gameuserDTO.setCommand(Info.WAIT);
			
			try {
				oos.writeObject(waitingroomchattingDTO);
				oos.writeObject(waitingroomuserDTO);
				oos.writeObject(waitingroomrcreateDTO);
				oos.writeObject(chatDTO);
				oos.writeObject(cloneList);
				oos.writeObject(gameuserDTO);
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			chatSend.setText("");
			
		}else if(e.getSource()==exitB||chatMsg.toLowerCase().equals("exit")) {
			chatDTO.setCommand(Info.EXIT);
			MembershipDAO membershipDAO = MembershipDAO.getInstance();
			membershipDAO.scoreUpdate(chatNickName, score);
			try {
				chatDTO.setCommand(Info.EXIT);
				
				oos.writeObject(waitingroomchattingDTO);
				oos.writeObject(waitingroomuserDTO);
				oos.writeObject(waitingroomrcreateDTO);
				oos.writeObject(chatDTO);
				oos.writeObject(cloneList);
				oos.writeObject(gameuserDTO);
				oos.flush();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		}else if(e.getSource() == answerT || e.getSource() == answerB){//★추가한 내용		
			
			if(answerT.getText().equals(answer)) {
				score += 20;
				waitingroomchattingDTO.setCommand(Info.WAIT);
				waitingroomuserDTO.setCommand(Info.WAIT);
				waitingroomrcreateDTO.setCommand(Info.WAIT);
				waitingroomrcreateDTO.setRoomName(roomName);
				chatDTO.setCommand(Info.ANSWER);
				chatDTO.setNickName(chatNickName);
				chatDTO.setMessage("["+chatNickName+"] 님 정답입니다");
				gameuserDTO.setCommand(Info.WAIT);
				
				try {
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO);
					oos.writeObject(cloneList);
					oos.writeObject(gameuserDTO);
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			
			answerT.setText("");
		}else if (e.getSource()==readyB) {//★추가한 내용
			chatMsg = "READY";
			waitingroomchattingDTO.setCommand(Info.WAIT);
			waitingroomuserDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setRoomName(roomName);
			chatDTO.setCommand(Info.READY);
			chatDTO.setMessage(chatMsg);
			chatDTO.setScore(score);
			gameuserDTO.setCommand(Info.WAIT);

			
			if(readyCount==0) {
				readyCount++;
				chatDTO.setReadyCount(readyCount);
				readyB.setEnabled(false);
				
			}else {
				readyCount++;
				chatDTO.setReadyCount(readyCount);
				readyB.setEnabled(false);
			}
			
			try {
				oos.writeObject(waitingroomchattingDTO);
				oos.writeObject(waitingroomuserDTO);
				oos.writeObject(waitingroomrcreateDTO);
				oos.writeObject(chatDTO);
				oos.writeObject(cloneList);
				oos.writeObject(gameuserDTO);
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			if(readyCount==4) {//if(readyCount==wrc.getPersonCB()) { 원래는 이거
				startB.setEnabled(true);
			}
		}
		
		else if(e.getSource()==startB) {//★추가한 내용
			can.setEnabled(true);
			timestop=true;
			answerT.setEditable(false);
			startB.setEnabled(false);
			
			if(startCount==0) {
				startCount++;
				chatDTO.setStartCount(startCount);
			}else {
				startCount++;
				chatDTO.setStartCount(startCount);
			}
			
			
			
			answerT.setText(Quiz[list[startCount-1]]);
			
			waitingroomchattingDTO.setCommand(Info.WAIT);
			waitingroomuserDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setRoomName(roomName);
			chatDTO.setCommand(Info.START);
			chatDTO.setMessage(answerT.getText());
			gameuserDTO.setCommand(Info.WAIT);
			
			if(startCount == 5) {
				startB.setEnabled(false);
				System.out.println("게임이 종료되었습니다");
				list = null;
			}
			
			try {
				oos.writeObject(waitingroomchattingDTO);
				oos.writeObject(waitingroomuserDTO);
				oos.writeObject(waitingroomrcreateDTO);
				oos.writeObject(chatDTO);
				oos.writeObject(cloneList);
				oos.writeObject(gameuserDTO);
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Thread start = new Thread(new Runnable() {
				catchmind_Canvas can = ChatFrame.this.can;
				@Override
				public void run() {
					for(int i=ii;i<=119;i++) {
						if(!timestop) break;
						try {
							Thread.sleep(100);//원래는 1000
								
						}catch(InterruptedException ee) {}
						bar.setValue(i);
						ii=i;
						bar.setString(i+1+"초");
						if(ii==119) {
							ii=0;
							bar.setString(ii+"초");
							startB.setEnabled(true);
						}
					}
					can.setEnabled(false);
				}
			});
			start.start();
		}else if(e.getSource() == clearB) {
			sendList.clear();
			cloneList.clear();
			can.repaint();
		}
		
		else {

			catchmind_ShapDTO dto = new catchmind_ShapDTO();
			
			if(e.getSource() == btn[0]) { colorNum = 0;
			}else if(e.getSource() == btn[1]) { colorNum = 1;
			}else if(e.getSource() == btn[2]) { colorNum = 2;
			}else if(e.getSource() == btn[3]) { colorNum = 3;
			}else if(e.getSource() == btn[4]) {	colorNum = 4;
			}else if(e.getSource() == btn[5]) {	colorNum = 5;
			}
		}
													//서버로  채팅 보내기
	}//actionPerformed 종료

	public void run() {		//서버로 부터 받는곳 
		WaitingRoomChattingDTO waitingroomchattingDTO = null;
		waitingRoomUserDTO waitingroomuserDTO = null;
		waitingRoomRCreateDTO waitingroomrcreateDTO = null;
		ChatDTO chatDTO = null;
		GameUserDTO gameuserDTO = null;
		
		while(true) {
			try {
				waitingroomchattingDTO = (WaitingRoomChattingDTO)ois.readObject();
				waitingroomuserDTO = (waitingRoomUserDTO)ois.readObject();
				waitingroomrcreateDTO = (waitingRoomRCreateDTO)ois.readObject();
				chatDTO=(ChatDTO)ois.readObject();
				serverInfoList = (ArrayList<catchmind_ShapDTO>) ois.readObject();
				gameuserDTO = (GameUserDTO) ois.readObject();
				
				//System.out.println("드레그시 받아온waitingroomrcreateDTO.getname"+ waitingroomrcreateDTO.getRoomName());
				
				if(waitingroomrcreateDTO.getRoomName().equals(roomName)) {
					if(serverInfoList != null) can.repaint();
				}

				
				
				
				//--------------------------------chatDTO -----------------------------------
				if(chatDTO.getCommand()==Info.EXIT){
					
//					oos.reset();
//					ois.close();
//					chatSocket.setKeepAlive(true);
//					dispose();
					
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						this.setVisible(false);
						dispose();
						waitingRoomClient.setVisible(true);
					}
					
					//System.exit(0);
				}else if(chatDTO.getCommand()==Info.SEND){
					
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						chatPrint.append(chatDTO.getMessage()+"\n");
						int chatPos = chatPrint.getText().length();
						chatPrint.setCaretPosition(chatPos);//스크롤바 위치 계속요청
					}
	

					
				}else if(chatDTO.getCommand()==Info.READY) {//★추가한 내용
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						readyCount = chatDTO.getReadyCount();
						System.out.println(readyCount);
						
						if(chatDTO.getReadyCount() == 2 && owner.equals("방장")) {
							startB.setEnabled(true);
							readyB.setEnabled(false);
						}
						
						System.out.println(chatDTO.getReadyCount());
						chatPrint.append(chatDTO.getMessage()+"\n");
	
						int chatPos = chatPrint.getText().length();
						chatPrint.setCaretPosition(chatPos);
					}
					
				}else if(chatDTO.getCommand()==Info.START) {//★추가한 내용
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						readyB.setEnabled(false);
						answer = chatDTO.getMessage();
						startCount = chatDTO.getStartCount();
						System.out.println(startCount);
						if(chatDTO.getStartCount() == 5) {
							startB.setEnabled(false);
							readyB.setEnabled(true);
						}
					}
				}else if(chatDTO.getCommand() == Info.ANSWER) {
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						chatPrint.append(chatDTO.getMessage()+"\n");
						
							if(chatDTO.getNickName().equals(chatNickName)) {
								score +=20;
								System.out.println("점수올랐습니다");
							}	
					}
						if(startCount == 5) {
							startB.setEnabled(false);
							readyB.setEnabled(true);
						}
				}
				
				//--------------대기방 유저 목록  ------------
				if(waitingroomuserDTO.getCommand()==Info.SEND) {
					chatNickName =waitingroomuserDTO.getName();
					point = waitingroomuserDTO.getScore();
				}
				
				
				//--------------------------방만들기 ------------------------
				if(waitingroomrcreateDTO.getCommand()==Info.CREATE) {
					
				}else if(waitingroomrcreateDTO.getCommand()==Info.WAIT) {

				}
				
				
	
				//-----------------gameuserDTO-------------------------------
				if(gameuserDTO.getCommand()==Info.JOIN) {

					if(waitingroomrcreateDTO.getRoomName().equals(roomName))chatModel.addElement(gameuserDTO);

					if(owner.equals("방장")){
						can.setEnabled(true);
						bar.setVisible(true);
					}

			
				}else if(gameuserDTO.getCommand()==Info.WAIT) {
				}
				

				
				
				
			}catch (IOException io) {
					io.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}	
		}//while문 종료
	}//run 종료
	
	public void getchatModel() {
		//DB의 모든 항목을 꺼내서 JList에 뿌리기
		ChatDAO chatDAO = ChatDAO.getInstance();//DAO를 참조하기위해 생성
		ArrayList<ChatDTO> chatList = chatDAO.getChatList();//DAO의 getFriendList()호출해서 반환값을 list에 저장	
		//for(ChatDTO dto : chatList) chatModel.addElement(dto);//list에 있는값 차례대로 출력	
	}
	
	
	
	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public JRadioButton getThinB() {
		return thinB;
	}

	public void setThinB(JRadioButton thinB) {
		this.thinB = thinB;
	}

	public JRadioButton getThickB() {
		return thickB;
	}

	public void setThickB(JRadioButton thickB) {
		this.thickB = thickB;
	}

	public ArrayList<catchmind_ShapDTO> getSendList() {
		return sendList;
	}
	public void setSendList(ArrayList<catchmind_ShapDTO> sendlist) {
		this.sendList = sendlist;
	}
	public ArrayList<catchmind_ShapDTO> getServerInfoList() {
		return serverInfoList;
	}

	public void setServerInfoList(ArrayList<catchmind_ShapDTO> serverInfoList) {
		this.serverInfoList = serverInfoList;
	}
	public int getColorNum() {
		return colorNum;
	}

	public void setColorNum(int colorNum) {
		this.colorNum = colorNum;
	}
	public JButton getClearB() {
		return clearB;
	}

	public void setClearB(JButton clearB) {
		this.clearB = clearB;
	}
/*
	public static void main(String[] args) 
	{
		ChatFrame chatframe= new ChatFrame();
		chatframe.service();
	}
*/
	
	public void service(waitingRoomRCreateDTO waitingroomrcreateDTO, waitingRoomUserDTO waitingroomuserDTO_send) {
	//public void service() {
		
		String chatServerIp="192.168.51.97";
		try {
		chatSocket =new Socket(chatServerIp, 9700);
		oos = new ObjectOutputStream(chatSocket.getOutputStream());
		ois = new ObjectInputStream(chatSocket.getInputStream());
		
		//방정보 받아오기 
		roomName = waitingroomrcreateDTO.getRoomName();	// 대기룸에서 방정보 획득
		//System.out.println("chat 서비스 생성시roomName"+roomName);
		
		roomPass = waitingroomrcreateDTO.getRoomPass();
		owner = waitingroomrcreateDTO.getOwner();
		person = waitingroomrcreateDTO.getPerson();
		roomNumber = waitingroomrcreateDTO.getRoomNumber();
		
		WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
		waitingroomchattingDTO.setCommand(Info.WAIT);
		waitingRoomUserDTO waitingroomuserDTO_1 = new waitingRoomUserDTO();
		waitingroomuserDTO_1.setCommand(Info.WAIT);
		
		waitingRoomRCreateDTO waitingroomrcreateDTO_1 = new waitingRoomRCreateDTO();
		waitingroomrcreateDTO_1.setCommand(Info.JOIN);
		waitingroomrcreateDTO_1.setRoomName(waitingroomrcreateDTO.getRoomName());
		waitingroomrcreateDTO_1.setRoomPass(roomPass);
		waitingroomrcreateDTO_1.setOwner(waitingroomrcreateDTO.getOwner());
		waitingroomrcreateDTO_1.setPerson(person);
		waitingroomrcreateDTO_1.setRoomNumber(roomNumber);
		
		ChatDTO chatDTO = new ChatDTO();
		chatDTO.setCommand(Info.JOIN);
		//chatDTO.setCommand(Info.WAIT);
		GameUserDTO gameuserDTO = new GameUserDTO();
		//gameuserDTO.setCommand(Info.WAIT);
		gameuserDTO.setCommand(Info.JOIN);
		gameuserDTO.setName(waitingroomuserDTO_send.getName());
		gameuserDTO.setPoint(waitingroomuserDTO_send.getScore());
		chatNickName = waitingroomuserDTO_send.getName();
		
		oos.writeObject(waitingroomchattingDTO);
		oos.writeObject(waitingroomuserDTO_1);
		oos.writeObject(waitingroomrcreateDTO_1);
		oos.writeObject(chatDTO);
		oos.writeObject(cloneList);
		oos.writeObject(gameuserDTO);
		oos.flush();

		
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		
		
		Thread chatThread =new Thread(this);
		chatThread.start();
	}
}

