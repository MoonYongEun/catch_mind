package miniproject.catchmind;

import java.awt.Color;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import miniproject.certificate.MailSendManager;
import miniproject.membership.dao.MembershipDAO;
import miniproject.membership.dto.MembershipDTO;


public class SignUpMember extends JFrame implements ActionListener{	
	private JLabel idL, passwordL, confirmPasswordL, nameL, addressL, 
					phoneNumberL, emailAddressL, certificateL;
	private JTextField idT, nameT;
	private JPasswordField passwordT, confirmPasswordT;
	private JTextField addressT, phoneNumberT1, phoneNumberT2, phoneNumberT3, 
						emailAddressT, certificateT;
	private JButton duplicationB, certificateB, cancelB, registerB;
	private JComboBox<String> mailSelect;
	private boolean duplicateCheck = true,//true: 중복존재, false:사용가능
					certificateCheck; //true: 인증키 일치(사용가능), false: 사용불가
	private String certificationKey;
	
	public SignUpMember() {
		super("회원가입");
		
		mailSelect = new JComboBox<String>();
		mailSelect.addItem("gmail.com");
		constructLabel();
		constructTextField();
		constructButton();
		event();
		
		
		Container container = this.getContentPane();
		container.setLayout(null);
		Panel leftP, IDP, passwordP, confirmPasswordP, nameP, addressP, 
				phoneP, emailP, certificateP;
		Panel rightP, IDRowP, passwordRowP, confirmPasswordRowP, nameRowP, addressRowP, 
				phoneRowP, emailRowP, certificateRowP;
		Panel buttonRowP;
		
		leftP = new Panel(new GridLayout(8,1));
		IDP = new Panel(new FlowLayout(FlowLayout.LEFT));
		passwordP = new Panel(new FlowLayout(FlowLayout.LEFT));
		confirmPasswordP = new Panel(new FlowLayout(FlowLayout.LEFT));
		nameP = new Panel(new FlowLayout(FlowLayout.LEFT));
		addressP = new Panel(new FlowLayout(FlowLayout.LEFT));
		phoneP = new Panel(new FlowLayout(FlowLayout.LEFT));
		emailP = new Panel(new FlowLayout(FlowLayout.LEFT));
		certificateP = new Panel(new FlowLayout(FlowLayout.LEFT));
		
		rightP = new Panel(new GridLayout(8, 1));
		IDRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		passwordRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		confirmPasswordRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		nameRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		addressRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		phoneRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		emailRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		certificateRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		
		buttonRowP = new Panel(new GridLayout(1, 2, 20, 10));
		
				
		IDP.add(idL);
		passwordP.add(passwordL);
		confirmPasswordP.add(confirmPasswordL);
		nameP.add(nameL);
		addressP.add(addressL);
		phoneP.add(phoneNumberL);
		emailP.add(emailAddressL);
		certificateP.add(certificateL);
				
		IDRowP.add(idT);
		IDRowP.add(duplicationB);
		passwordRowP.add(passwordT);
		passwordRowP.add(new JLabel("(5~12자 이내로 입력)"));
		confirmPasswordRowP.add(confirmPasswordT);
		confirmPasswordRowP.add(new JLabel("(한 번 더 입력)"));
		nameRowP.add(nameT);
		nameRowP.add(new JLabel("(한글 최대 6자리 가능)"));
		addressRowP.add(addressT);
		phoneRowP.add(phoneNumberT1);
		phoneRowP.add(new JLabel(" -  "));
		phoneRowP.add(phoneNumberT2);
		phoneRowP.add(new JLabel(" -  "));
		phoneRowP.add(phoneNumberT3);
		emailRowP.add(emailAddressT);
		emailRowP.add(new JLabel("@"));
		emailRowP.add(mailSelect);
		certificateB.setSize(getPreferredSize());
		certificateRowP.add(certificateB);
		certificateRowP.add(new JLabel("      인증번호 :"));
		certificateRowP.add(certificateT);
		
		leftP.setBounds(20, 20, 100, 300);	
		rightP.setBounds(120, 20, 250, 300);
		buttonRowP.setBounds(20, 330, 350, 50);
		
		leftP.add(IDP);
		leftP.add(passwordP);
		leftP.add(confirmPasswordP);
		leftP.add(nameP);
		leftP.add(addressP);
		leftP.add(phoneP);
		leftP.add(emailP);
		leftP.add(certificateP);
		
		rightP.add(IDRowP);	
		rightP.add(passwordRowP);
		rightP.add(confirmPasswordRowP);
		rightP.add(nameRowP);
		rightP.add(addressRowP);
		rightP.add(phoneRowP);
		rightP.add(emailRowP);
		rightP.add(certificateRowP);		
	
		buttonRowP.add(cancelB);
		buttonRowP.add(registerB);
				
		container.add(leftP);
		container.add(rightP);
		container.add(buttonRowP);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = 400;
		int frameH = 420;
		setBounds((dimension.width/2-frameW/2), (dimension.height/2-frameH/2), frameW, frameH);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}//constr

	private void event() {
		cancelB.addActionListener(this);
		registerB.addActionListener(this);
		duplicationB.addActionListener(this);
		certificateB.addActionListener(this);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				SignUpMember.this.setVisible(false);
				SignUpMember.this.dispose();
			}
		});
	}

	private void constructLabel() {
		idL = new JLabel("아이디(ID) *");
		passwordL = new JLabel("비밀번호 *");
		confirmPasswordL = new JLabel("비밀번호 확인 *");
		nameL = new JLabel("이름 *");
		addressL = new JLabel("주소");
		phoneNumberL = new JLabel("휴대폰 번호");
		emailAddressL = new JLabel("이메일 *");	
		certificateL = new JLabel("인증 메일 발송 :");
	}//constructLabel

	private void constructTextField() {
		idT = new JTextField(12);
		idT.setDocument(new JTextFieldLimit(10));
		passwordT = new JPasswordField(8);
		passwordT.setDocument(new JTextFieldLimit(12));
		confirmPasswordT = new JPasswordField(8);
		confirmPasswordT.setDocument(new JTextFieldLimit(12));
		nameT = new JTextField(8);
		nameT.setDocument(new JTextFieldLimit(12));
		addressT = new JTextField(21);
		phoneNumberT1 = new JTextField(4);
		phoneNumberT1.setDocument(new JTextFieldLimit(3));
		phoneNumberT2 = new JTextField(5);
		phoneNumberT2.setDocument(new JTextFieldLimit(4));
		phoneNumberT3 = new JTextField(5);
		phoneNumberT3.setDocument(new JTextFieldLimit(4));
		emailAddressT = new JTextField(10);
		certificateT = new JTextField(8);
	}//constructTextField

	private void constructButton() {
		duplicationB = new JButton("중복 확인");
		duplicationB.setBackground(new Color(250,250,190));
		cancelB = new JButton("취소");
		cancelB.setBackground(Color.gray);
		registerB = new JButton(" 작성 완료 ");
		certificateB = new JButton("발송");
		certificateB.setBackground(new Color(250,250,190));
	}//constructButton
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("발송")) mailSend();
		else if(e.getActionCommand().equals("취소")) {
			setVisible(false);
			dispose();
		}		
		else if(e.getActionCommand().equals("중복 확인")) checkID();
		else if(e.getSource()==registerB) memberRegister();		
	}
	
	//회원목록의 DB에 접근하여 입력한 ID가 이미 존재하는지 검사한다.
	private void checkID() {//true: 중복존재, false: 사용가능
		String userID = idT.getText().trim();
		if(userID.equals("")) {
			JOptionPane.showMessageDialog(this, "아이디(ID)는 필수 입력사항입니다.");
			return;
		}
		else if(userID.length()<3) {
			JOptionPane.showMessageDialog(this, "아이디(ID)는 3글자 이상 입력하세요.");
			return;
		}
		//DB 중복체크
		MembershipDAO membershipDAO = MembershipDAO.getInstance();
		duplicateCheck = membershipDAO.isIDExist(userID);
		if(!duplicateCheck) JOptionPane.showMessageDialog(this, "사용 가능한 아이디 입니다.");
		else {
			JOptionPane.showMessageDialog(this, "이미 존재하는 아이디 입니다."
												+ "\n다른 아이디를 입력해 주세요.");
		}
	}

	//사용자가 입력한 메일주소로 인증메일을 발송한다.(발신자:songming05)
	private void mailSend() {//true: 중복존재, false: 사용가능
		String userEmail = emailAddressT.getText().trim();
		//DB 중복체크
		boolean emailExist=false;
		MembershipDAO membershipDAO = MembershipDAO.getInstance();
		emailExist = membershipDAO.isEmailExist(userEmail);
		if(emailExist) {
			JOptionPane.showMessageDialog(this, "이미 존재하는 이메일 입니다."
									+ "\n다른 이메일 주소를 입력해 주세요.");
			return;
		}
		
		MailSendManager mailSendManager = new MailSendManager(userEmail);
		
		certificationKey = mailSendManager.getCertificationKey();
		if(certificationKey.length()>4) JOptionPane.showMessageDialog(this, "인증 메일이 발송되었습니다.");
	}
	
	/*
	 * 회원DB에 올리기 전에 모든 적합성 검사를 한다.
	 * ID는 10글자 이상 입력불가, password는 12자 이상 불가
	 * 이름은 영어 최대12자, 한글 최대 6자
	 */
	private void memberRegister() {
		String userID = idT.getText().trim();
		if(userID.equals("")) {
			JOptionPane.showMessageDialog(this, "아이디(ID)는 필수 입력사항입니다.");
			return;
		}
		if(duplicateCheck) {
			JOptionPane.showMessageDialog(this, "아이디(ID) 중복확인을 해주시기 바랍니다.");
			return;
		}
		String userPassword = passwordT.getText().trim();
		if(userPassword.equals("")) {
			JOptionPane.showMessageDialog(this, "비밀번호는 필수 입력사항입니다.");
			return;
		}
		else if(userPassword.length()<5) {
			JOptionPane.showMessageDialog(this, "비밀번호는 5글자 이상 입력하세요.");
			return;
		}
		String confirm = confirmPasswordT.getText().trim();
		if(!userPassword.equals(confirm)) {
			JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
			return;
		}
		String userName = nameT.getText().trim();
		if(userName.equals("")) {
			JOptionPane.showMessageDialog(this, "이름은 필수 입력사항입니다.");
			return;
		}
		byte[] nameByte = userName.getBytes();
		int engCount=0, korCount=0;
		for(byte data : nameByte) {
			if(data>0)	engCount++;
			else if(data<0) korCount++;
		}
		if(korCount+engCount>12) {
			JOptionPane.showMessageDialog(this, "이름은 한글 6자, 영어 12자 까지 가능합니다.");
			return;
		}
		String userAddress = addressT.getText().trim();
		String userPhone = phoneNumberT1.getText().trim()
							+ phoneNumberT2.getText().trim()
							+ phoneNumberT3.getText().trim();
		String userEmail = emailAddressT.getText().trim()+"@gmail.com";
		if(userEmail.equals("")) {
			JOptionPane.showMessageDialog(this, "이메일은 필수 입력사항입니다.");
			return;
		}
		
		String inputKey = certificateT.getText().trim();		
		if(inputKey.equals(certificationKey)) {
			certificateCheck = true;
			JOptionPane.showMessageDialog(this, "이메일 인증이 완료되었습니다.");
		}
		if(!certificateCheck) JOptionPane.showMessageDialog(this, "이메일 인증이 되지 않았습니다.");
		
		if(certificateCheck && !duplicateCheck) {
			//MembershipDTO 에 추가
			MembershipDTO membershipDTO = new MembershipDTO();
			MembershipDAO membershipDAO = MembershipDAO.getInstance();
			int sequence = membershipDAO.getSeq();
			
			membershipDTO.setSequence(sequence);
			membershipDTO.setId(userID);
			membershipDTO.setPassword(userPassword);
			membershipDTO.setName(userName);
			membershipDTO.setAddress(userAddress);
			membershipDTO.setPhone(userPhone);
			membershipDTO.setEmail(userEmail);
			
			//DB 에 등록
			membershipDAO.insertArticle(membershipDTO);
			
			JOptionPane.showMessageDialog(this, "환영합니다!! "+userName+"님"
											+ "\n회원가입이 완료 되었습니다."
											+ "\n로그인창으로 이동합니다.");
			//로그인 창으로
			setVisible(false);
			dispose();
		}
		
	}

}
