package miniproject.catchmind;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import miniproject.membership.dao.MembershipDAO;

public class waitingRoomMyInfo extends JFrame {
	JLabel idL,nickNameL, pointL;
	JTextField idF,nickNameF,pointF;
	JButton cancleB, myIB;
	
	private ImageIcon inFoImage;//
	private JScrollPane inFoScrollPane;//
	private String iconImg;
	
	public void myinfoC(String img, String id) {
		setLayout(null);
		idL = new JLabel(" I D ");
		idF = new JTextField(3);
		nickNameL = new JLabel(" 닉네임 ");
		nickNameF = new JTextField(10);
		pointL = new JLabel(" 내 점수 ");
		pointF = new JTextField(10);
		cancleB = new JButton(" 닫  기 ");
		
		//
		if(img.equals("cong.png")) {
			iconImg=img;
		}
		else if(img.equals("ag.png")) {
			iconImg=img;
		}
		else if(img.equals("cona.png")) {
			iconImg=img;
		}
		else if(img.equals("dora.png")) {
			iconImg=img;
		}
		else if(img.equals("bo.png")) {
			iconImg=img;
		}
		else if(img.equals("je.png")) {
			iconImg=img;
		}
		else if(img.equals("ru.png")) {
			iconImg=img;
		}
		else if(img.equals("jang.png")) {
			iconImg=img;
		}
		else if(img.equals("pica.png")) {
			iconImg=img;
		}
		
		myIB = new JButton(new ImageIcon(iconImg));
		myIB.setEnabled(true);
		//
		
		myIB.setBounds(10,10,100,220);
		idL.setBounds(150,40,50,30);
		idF.setBounds(200,40,50,30);
		nickNameL.setBounds(150,75,50,30);
		nickNameF.setBounds(200,75,50,30);
		pointL.setBounds(150,110,50,30);
		pointF.setBounds(200,110,50,30);
		cancleB.setBounds(150,200,100,30);
		
		idF.setEditable(false);
		nickNameF.setEditable(false);
		pointF.setEditable(false);
		
		add(myIB);
		add(idL); add(idF);
		add(nickNameL); add (nickNameF);
		add(pointL); add(pointF);
		add(cancleB);
		
		//
		inFoImage = new ImageIcon("create.jpg");
        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(inFoImage.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
            }     
        };
        background.setLayout(null);
        
        background.add(myIB);
        background.add(idL);
        background.add(idF);
        background.add(nickNameL);
        background.add(nickNameF);
        background.add(pointL);
        background.add(pointF);
        background.add(cancleB);
        
        inFoScrollPane = new JScrollPane(background);
        setContentPane(inFoScrollPane);
		//
        MembershipDAO membershipDAO = MembershipDAO.getInstance();
        idF.setText(id);
        nickNameF.setText(membershipDAO.getName(id));
        pointF.setText(membershipDAO.getScore(id)+"");
		
		setTitle("내 정보");
		setBounds(750,300,300,300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		
		cancleB.addActionListener(new ActionListener() {
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
}
