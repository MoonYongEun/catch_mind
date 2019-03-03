package miniproject.catchmind;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class catchmind_Canvas extends Canvas /*implements Runnable*/{
	//private ChatFrame chat;
	private ChatFrame mp;
	private Image bufferImg;
	private Graphics bufferG;
	
	public catchmind_Canvas(ChatFrame mp){
		this.mp = mp;
		setBackground(new Color(255, 255 ,255));		
	}
	
	@Override
	public void update(Graphics g){
		Dimension d = getSize(); //사이즈 확인
		if(bufferG == null){//Graphics의 값이 아무것도 없는지 확인
			bufferImg = createImage(d.width, d.height); //캔버스의 높이와 길이만큼 그려라
			bufferG = bufferImg.getGraphics(); //만든 이미지를 bufferG에 저장
		}		
		bufferG.setColor(getBackground()); //캔버스 바탕색
		bufferG.fillRect(0, 0, d.width, d.height); //그색을 캔버스 크기만큼 채워라(그전껄 지움)
		
		if(mp.getServerInfoList() != null) {
			for(catchmind_ShapDTO dto : mp.getServerInfoList()) {
				int x1 = dto.getX1(); //getter로 x1값 가져오기 
				int y1 = dto.getY1(); //getter로 y1값 가져오기 
				int x2 = dto.getX2(); //getter로 x2값 가져오기 
				int y2 = dto.getY2(); //getter로 y2값 가져오기 				
				switch(dto.getColorNum()) {
					case 0 : bufferG.setColor(Color.BLACK); break; //검은색
					case 1 : bufferG.setColor(Color.RED); break; //빨강색
					case 2 : bufferG.setColor(Color.GREEN); break; //초록색
					case 3 : bufferG.setColor(Color.BLUE); break; //파랑색
					case 4 : bufferG.setColor(Color.YELLOW); break; //노랑색
					case 5 : bufferG.setColor(Color.MAGENTA); break; //분홍색
				}
				if(dto.getShape() == Shape.LINE) {
					Graphics2D graphics2d = (Graphics2D) bufferG;
			         graphics2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, 0));
					bufferG.drawLine(x1, y1, x2, y2); //얇은 펜
				}else if(dto.getShape() == Shape.RECT) {
					Graphics2D graphics2d = (Graphics2D) bufferG;
			         graphics2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, 0));
			        bufferG.drawLine(x1,y1,x2,y2);
				}
			}
		}
		
		if(mp.getSendList().size() ==0) {//list가 없을때 최초만 기동
			int x1 = mp.getX1(); //getter로 x1값 가져오기 
			int y1 = mp.getY1(); //getter로 y1값 가져오기 
			int x2 = mp.getX2(); //getter로 x2값 가져오기 
			int y2 = mp.getY2(); //getter로 y2값 가져오기 
			
			switch(mp.getColorNum()) {
				case 0 : bufferG.setColor(Color.BLACK); break;
				case 1 : bufferG.setColor(Color.RED); break;
				case 2 : bufferG.setColor(Color.GREEN); break;
				case 3 : bufferG.setColor(Color.BLUE); break;
				case 4 : bufferG.setColor(Color.YELLOW); break;
				case 5 : bufferG.setColor(Color.MAGENTA); break;
			}
			if(mp.getThinB().isSelected()) {
				Graphics2D graphics2d = (Graphics2D) bufferG;
		         graphics2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, 0));
				bufferG.drawLine(x1, y1, x2, y2); //얇은 펜
			}
			else if(mp.getThickB().isSelected()) {
				Graphics2D graphics2d = (Graphics2D) bufferG;
		         graphics2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, 0));
		        bufferG.drawLine(x1,y1,x2,y2);			
			}
		}
		if(mp.getClearB().isSelected()) bufferG.clearRect(0, 0, d.width, d.height);
		paint(g); 
	}
	
	@Override
	public void paint(Graphics g){
		g.drawImage(bufferImg, 0, 0, this);
	}


}
