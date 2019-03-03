package miniproject.catchmind;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import miniproject.membership.dao.MembershipDAO;

public class LogInWindow extends JFrame implements ActionListener{
	private JLabel idL, passwordL;
	private JTextField idT;
	private JPasswordField passwordT;
	private JButton cancelB, joinB, loginB;
	private JRadioButton findIDB, findPasswordB;
	
	private ImageIcon icon;//
	private JScrollPane scrollPane;//
	
	public static void main(String[] args) {
		
		LogInWindow logInWindow = new LogInWindow();
		while(true) {
			try {
				logInWindow.startBGM(0);
				Thread.sleep(152000);
				logInWindow.startBGM(1);
				Thread.sleep(190000);
				logInWindow.startBGM(2);
				Thread.sleep(201000);
				logInWindow.startBGM(3);
				Thread.sleep(150000);
				logInWindow.startBGM(4);
				Thread.sleep(118000);
				//break;
			} catch(Exception e) {
				e.printStackTrace();				
			}
		}
	}
	
	public LogInWindow() {
		super("캐치마인드 로그인");
		
		constructField();
		event();		
		
		setLayout(null);
		Container container = this.getContentPane();
		Panel labelP, textP, loginP, findID_PasswordP;
		labelP = new Panel(new GridLayout(2,1,5,10));
		textP = new Panel(new GridLayout(2,1,5,20));
		loginP = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		findID_PasswordP =  new Panel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		labelP.setBounds(20, 20, 100, 80);		
		textP.setBounds(120, 20, 150, 80);
		loginP.setBounds(20, 120, 250, 40);
		findID_PasswordP.setBounds(20, 160, 250, 30);
		
		labelP.setBackground(new Color(230,255,255));	
		textP.setBackground(new Color(230,255,255));
		loginP.setBackground(new Color(230,255,255));
		findID_PasswordP.setBackground(new Color(230,255,255));
		
		
		labelP.add(idL);
		labelP.add(passwordL);
		textP.add(idT);
		textP.add(passwordT);
		loginP.add(cancelB);
		loginP.add(joinB);
		loginP.add(loginB);
		findID_PasswordP.add(findIDB);
		findID_PasswordP.add(findPasswordB);
		
		container.add(labelP);
		container.add(textP);
		container.add(loginP);
		container.add(findID_PasswordP);
		
		
		//이미지 넣을 수 있으면 넣자
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = 300;
		int frameH = 215;
		setBounds((dimension.width/2-frameW/2), (dimension.height/2-frameH/2), frameW, frameH);
		
		icon = new ImageIcon("login.jpg");
        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(icon.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
            }     
        };
        background.setLayout(null);
        
        background.add(labelP);
        background.add(textP);
        background.add(loginP);
        background.add(findID_PasswordP);
        
        scrollPane = new JScrollPane(background);
        setContentPane(scrollPane);
		
		
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
		
		
		
	}



	private void event() {
		cancelB.addActionListener(this);
		joinB.addActionListener(this);
		loginB.addActionListener(this);
		passwordT.addActionListener(this);
		findIDB.addActionListener(this);
		findPasswordB.addActionListener(this);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				int confirm = JOptionPane.showConfirmDialog(LogInWindow.this, "정말로 종료하시겠습니까?");
				if(confirm==JOptionPane.YES_OPTION) System.exit(0);
				else return;
			}
		});
	}

	private void constructField() {
		idL = new JLabel("아이디(ID) 입력 :");
		passwordL = new JLabel("비밀번호 입력 :");
		idT = new JTextField(10);
		passwordT = new JPasswordField(10);
		cancelB = new JButton("취소");
		joinB = new JButton("회원가입");
		joinB.setToolTipText("회원가입 창으로 이동합니다");
		loginB = new JButton("로그인");
		findIDB = new JRadioButton("아이디 /", new ImageIcon());
		findPasswordB = new JRadioButton("비밀번호 찾기", new ImageIcon());	
		findIDB.setToolTipText("아이디 찾기");
		findPasswordB.setToolTipText("비밀번호 찾기");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cancelB) {
			int confirm = JOptionPane.showConfirmDialog(this, "정말로 종료하시겠습니까?");			
			if(confirm==JOptionPane.YES_OPTION) System.exit(0);
			else return;
		} 
		else if(e.getSource()==joinB) {
			SignUpMember signUpMember = new SignUpMember();			
		} 
		else if(e.getSource()==loginB || e.getSource()==passwordT) {
			String userID = idT.getText().trim();
			String userPWD = passwordT.getText().trim();
			
			MembershipDAO membershipDAO = MembershipDAO.getInstance();
			boolean idCheck = membershipDAO.isIDCorrespond(userID);
			boolean passwordCheck = membershipDAO.isPasswordCorrespond(userID, userPWD);
			if(!idCheck) {
				JOptionPane.showMessageDialog(this, "존재하지 않는 아이디 입니다.");
				return;				
			}
			else if(!passwordCheck) {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
				return;				
			}
			else {
				JOptionPane.showMessageDialog(this, "로그인 성공!");
				
				String userName = membershipDAO.getName(userID);
				int userScore = membershipDAO.getScore(userID);
				IdNameScoreDTO idnamescoreDTO = new IdNameScoreDTO();
				idnamescoreDTO.setId(userID);
				idnamescoreDTO.setName(userName);
				idnamescoreDTO.setScore(userScore);
				
				//대기실 창 생성!
				WaitingRoomClient waitingroomclient = new WaitingRoomClient();
				waitingroomclient.event();
				waitingroomclient.service(idnamescoreDTO);			
				
				this.setVisible(false);
				this.dispose();
				return;				
			}
		} else if(e.getSource()==findIDB) {
			String findID="";
			FindIDorPassword findIDorPassword = new FindIDorPassword(findID);			
		} else if(e.getSource()==findPasswordB) {
			String findID="";
			String findPassword="";
			FindIDorPassword findIDorPassword = new FindIDorPassword(findID, findPassword);			
		}
	}
	
	public void startBGM(int num) {
		File[] bgmArray = new File[5];
		AudioInputStream audioInputStream;
		AudioFormat audioFormat;
		DataLine.Info info;
		
		//프로젝트폴더에 음원 추가해야한다.
		bgmArray[0] = new File("Lazy_Rock.wav"); 
		bgmArray[1] = new File("Lucid_Dreamer.wav");
		bgmArray[2] = new File("Fond_Memories.wav");
		bgmArray[3] = new File("If_I_Had_a_Chicken.wav");
		bgmArray[4] = new File("From_the_Fall.wav");
		
		Clip clip;
		
		try {
			audioInputStream = AudioSystem.getAudioInputStream(bgmArray[num]);
			audioFormat = audioInputStream.getFormat();
			info = new DataLine.Info(Clip.class, audioFormat);
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(audioInputStream);
			clip.start();
			
		} catch (Exception e) {
			System.out.println("error : " + e);
		}
		
	}

}
