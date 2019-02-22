package miniproject.membership.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import miniproject.membership.dto.MembershipDTO;

public class MembershipDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.51.79:1521:xe";
	private String user = "java";
	private String password = "itbank";
	
	private static MembershipDAO instance;	
	public static MembershipDAO getInstance() {
		if(instance==null) {
			synchronized (MembershipDAO.class) {
				instance = new MembershipDAO();
			}
		}
		return instance;
	}	
	
	public MembershipDAO() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}//constr
	
	public Connection getConnection(){
		Connection conn=null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return conn;
	}//getConnection
	
	//DB의 시퀀스객체에 접근하여 시퀀스 번호를 받아온다.
	public int getSeq() {
		int seq = 0;
		Connection conn = null;
		PreparedStatement prps = null;		
		ResultSet rs = null;
		
		String sql = "select seq_membership.nextval from dual";
			
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			rs = prps.executeQuery();
			rs.next();
			seq = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return seq;
	}//getSeq

	//membershipDTO를 통해 데이터를 꺼내오며, 그 데이터를 회원가입 DB에 insert 한다.
	public void insertArticle(MembershipDTO membershipDTO) {
		String sql = "insert into membership values(?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement prps = null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, membershipDTO.getSequence());
			prps.setString(2, membershipDTO.getId());
			prps.setString(3, membershipDTO.getPassword());
			prps.setString(4, membershipDTO.getName());
			prps.setString(5, membershipDTO.getAddress());
			prps.setString(6, membershipDTO.getPhone());
			prps.setString(7, membershipDTO.getEmail());
			prps.setInt(8, membershipDTO.getTotalScore());
			
			prps.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//insertArticle
	
	//ID중복체크 과정
	public boolean isIDExist(String userID) {//true: 사용가능, false: 사용불가 
		boolean exist=false;
		String sql = "select * from membership where id = ?";
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		int sw=0;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, userID);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				exist = false;
				sw=1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(sw==0) exist=true;
		return exist;
	}//isIDExist
	

	public boolean isIDCorrespond(String userID) {
		boolean idCheck=false;
		String sql = "select * from membership where id = ?";
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, userID);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				idCheck = true;
				return idCheck;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idCheck;
	}

	public boolean isPasswordCorrespond(String userID, String userPWD) {
		boolean idNPasswordCheck=false;
		String sql = "select * from membership where id = ?";
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, userID);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				String checkPassword = rs.getString("password");
				if(userPWD.equals(checkPassword)) {
					idNPasswordCheck = true;
				}
				else return idNPasswordCheck;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idNPasswordCheck;
	}

	public String getName(String userID) {
		String sql = "select * from membership where id = ?";
		String name="";
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, userID);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				name = rs.getString("name");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return name;
		
	}

	public int getScore(String userID) {
		int score=0;
		String sql = "select * from membership where id = ?";
		
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, userID);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				score = rs.getInt("total_score");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return score;
		
	}
	
}
