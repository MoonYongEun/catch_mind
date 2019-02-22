package miniproject.catchmind;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class waitingRoomMyInfo extends JFrame {
	JLabel idL,nickNameL, pointL;
	JTextField idF,nickNameF,pointF;
	JButton cancleB, myIB;
	
	public void myinfoC() {
		setLayout(null);
		idL = new JLabel(" I D ");
		idF = new JTextField(3);
		nickNameL = new JLabel(" 닉네임 ");
		nickNameF = new JTextField(10);
		pointL = new JLabel(" 내 점수 ");
		pointF = new JTextField(10);
		cancleB = new JButton(" 닫  기 ");
		
		myIB = new JButton(new ImageIcon("red.png"));
		myIB.setEnabled(false);
		
		myIB.setBounds(10,10,100,220);
		idL.setBounds(150,40,50,30);
		idF.setBounds(200,40,50,30);
		nickNameL.setBounds(150,75,50,30);
		nickNameF.setBounds(200,75,50,30);
		pointL.setBounds(150,110,50,30);
		pointF.setBounds(200,110,50,30);
		cancleB.setBounds(150,200,100,30);
		
		add(myIB);
		add(idL); add(idF);
		add(nickNameL); add (nickNameF);
		add(pointL); add(pointF);
		add(cancleB);
		
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
