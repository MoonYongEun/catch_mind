package miniproject.catchmind;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import miniproject.catchmind.ChatDTO;

public class ChatDAO {
	private String chatDriver = "oracle.jdbc.driver.OracleDriver";
	private String chatUrl = "jdbc:oracle:thin:@localhost:1521:xe";
	private String chatUser = "java";
	private String chatPassword = "itbank";
	
	private static ChatDAO instance; 
	
	public static ChatDAO getInstance() {
		synchronized(ChatDAO.class) {
			if(instance==null) {
				instance = new ChatDAO();
			}
		}
		
		return instance;
	}

	
	public ChatDAO() {
		try {
			Class.forName(chatDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		Connection chatConn = null;

		try {
			chatConn = DriverManager.getConnection(chatUrl, chatUser, chatPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chatConn;
	}

	
	public int getSeq() {
		int seq=0;//시퀀스객체값이 0이 들어오면 뭔가 잘못된것임
		Connection chatConn = getConnection();//이거 3개는 세트인거같다
		PreparedStatement pstmt = null;//이거 3개는 세트인거같다
		ResultSet rs = null;//이거 3개는 세트인거같다
		String sql = "select seq_membership.nextval from dual";
		
		try {
			pstmt = chatConn.prepareStatement(sql);//생성
			rs = pstmt.executeQuery();//실행
			
			rs.next();
			seq = rs.getInt(1);// 1번 컬럼의 값을 seq에 저장하라는 뜻
			//오라클의 컬럼(세로줄)은 0부터 시작 하는게 아니라 1부터 시작하는 것이다
			//시퀸스값은 무조건 1부터 들어간다
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(chatConn!=null) chatConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return seq;
	}

	public void insert(ChatDTO chatDTO) {
		Connection conn = getConnection();
		
		PreparedStatement pstmt = null;
		String sql = "insert into membership values(?,?,?,?,?)";
		//(seq, id,password,name,email) values(SEQ_MEMBERSHIP.NEXTVAL,'aaa','33333','삼에이','aaa@gmail.com')";
		
		try {
			pstmt = conn.prepareStatement(sql);//생성
			pstmt.setInt(1, chatDTO.getSeq());
			pstmt.setString(2, chatDTO.getId());
			pstmt.setString(3, chatDTO.getPassword());
			pstmt.setString(4, chatDTO.getEmail());
			//pstmt.setString(2, chatDTO.getNickName());
			//pstmt.setString(3, chatDTO.get);
			
			pstmt.executeUpdate();//실행
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<ChatDTO> getChatList() {
		ArrayList<ChatDTO> list = new ArrayList<ChatDTO>();//DTO를 참고하기위해 생성
		Connection chatConn = getConnection();//이거 3개는 세트인거같다
		PreparedStatement pstmt = null;//이거 3개는 세트인거같다
		ResultSet rs = null;//이거 3개는 세트인거같다
		String sql = "select * from membership";//friend에 있는 모든 데이터 불러온다
		
		try {
			pstmt = chatConn.prepareStatement(sql);//생성
			rs = pstmt.executeQuery();//실행
			
			while(rs.next()) {//데이터가 비어있을때 까지 읽어들임
				ChatDTO chatDTO = new ChatDTO();//참고하기위해 객체 생성
				chatDTO.setSeq(rs.getInt("seq"));//DB에서 데이터불러 와서 DTO에 저장
				chatDTO.setNickName(rs.getString("name"));//DB에서 데이터불러 와서 DTO에 저장
				
				list.add(chatDTO);//list에   DB에서 가져온 DTO데이터 담아줌
			}//while
			
		} catch (SQLException e) {
			e.printStackTrace();
			list = null;//try catch구간에서 에러발생시 list에 null을 넣어서 더이상 진행이 안되게 해줌
		}finally {
			try {
				if(rs!=null) rs.close();//닫을때는 열었던순서와 거꾸로 닫아준다
				if(pstmt!=null) pstmt.close();//닫을때는 열었던순서와 거꾸로 닫아준다
				if(chatConn!=null) chatConn.close();//닫을때는 열었던순서와 거꾸로 닫아준다
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;//FriendManager의   ArrayList<FriendDTO> list에 값 전달
	}


	public void delete(int seq) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "delete from membership where seq=?";
		
		try {
			pstmt = conn.prepareStatement(sql);//생성
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();//실행
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();//닫을때는 열었던순서와 거꾸로 닫아준다
				if(conn!=null) conn.close();//닫을때는 열었던순서와 거꾸로 닫아준다
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

