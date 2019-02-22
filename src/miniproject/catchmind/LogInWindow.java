package miniproject.catchmind;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import miniproject.membership.dao.MembershipDAO;

public class LogInWindow extends JFrame implements ActionListener{
	private JLabel idL, passwordL;
	private JTextField idT;
	private JPasswordField passwordT;
	private JButton cancelB, joinB, loginB;
	
	public static void main(String[] args) {
		LogInWindow logInWindow = new LogInWindow();
	}
	
	public LogInWindow() {
		super("캐치마인드 로그인");	
		
		constructField();
		event();
		
		
		setLayout(null);
		Container container = this.getContentPane();
		Panel labelP, textP, loginP;
		labelP = new Panel(new GridLayout(2,1,5,10));
		textP = new Panel(new GridLayout(2,1,5,20));
		loginP = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		labelP.setBounds(20, 20, 100, 80);		
		textP.setBounds(120, 20, 150, 80);
		loginP.setBounds(20, 120, 260, 40);
		
		labelP.add(idL);
		labelP.add(passwordL);
		textP.add(idT);
		textP.add(passwordT);
		loginP.add(cancelB);
		loginP.add(joinB);
		loginP.add(loginB);
		
		
		//labelP.setBackground(Color.DARK_GRAY);
		//textP.setBackground(Color.WHITE);
		//loginP.setBackground(Color.pink);;
		
		container.add(labelP);
		container.add(textP);
		container.add(loginP);
		
		//이미지 넣을 수 있으면 넣자
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = 300;
		int frameH = 200;
		setBounds((dimension.width/2-frameW/2), (dimension.height/2-frameH/2), frameW, frameH);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	private void event() {
		cancelB.addActionListener(this);
		joinB.addActionListener(this);
		loginB.addActionListener(this);
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
		loginB = new JButton("로그인");		
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
		else if(e.getSource()==loginB) {
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
				//memebershipDTO 전송!
				
				String userName = membershipDAO.getName(userID);
				int userScore = membershipDAO.getScore(userID);
				IdNameScoreDTO idnamescoreDTO = new IdNameScoreDTO();
				idnamescoreDTO.setId(userID);
				idnamescoreDTO.setName(userName);
				idnamescoreDTO.setScore(userScore);
				
				//대기실 창 생성!
				//WaitingRoomClient waitingroomclient = new WaitingRoomClient();
				//waitingroomclient.event();
				//waitingroomclient.service(idnamescoreDTO);
			
				
				this.setVisible(false);
				this.dispose();
				return;				
			}
		}
	}

}
