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
	public boolean isIDExist(String userID) {//true: 중복 존재, false: 사용가능
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
		if(sw==1) exist=true;
		return exist;
	}//isIDExist
	
	//이메일 중복체크 과정
	public boolean isEmailExist(String userEmail) {
		boolean exist=false;
		String sql = "select * from membership where email = ?";
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		int sw=0;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, userEmail);
			rs = prps.executeQuery();
			
			if(rs.next()) {
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
		if(sw==1) exist=true;
		return exist;
		
	}
	

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
		String name="";
		Connection conn=getConnection();
		PreparedStatement prps=null;
		ResultSet rs=null;
		
		String sql = "select * from membership where id = ?";
		try {
			prps = conn.prepareStatement(sql);
			prps.setString(1, userID);
			rs = prps.executeQuery();
			
			while(rs.next()) {
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
			
			while(rs.next()) {
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

	public String findID(String inputName, String inputEmail) {
		String sql = "select * from membership where name = ? and email = ? ";
		String resultID="";
		
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, inputName);
			prps.setString(2, inputEmail);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				resultID = rs.getString("id");
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
		return resultID;		
	}

	public String findPassword(String inputID, String inputEmail) {
		String sql = "select * from membership where id = ? and email = ? ";
		String resultPassword="";
		
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, inputID);
			prps.setString(2, inputEmail);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				resultPassword = rs.getString("password");
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
		return resultPassword;
	}

	public int getLoginCount() {
		int result = 0;
		String sql = "select * from membership where login = ?";
		
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, 1);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				result++;
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
		return result;
	}

	public void setLogin(String nickName) {
		String sql = "update membership set login = 1 where name = ?";
		
		Connection conn=null;
		PreparedStatement prps=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, nickName);
			
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
	}
	
	public void scoreUpdate(String userID, int getScore) {
		String sql = "update membership set total_score = total_score+? where id = ?";
		
		Connection conn=null;
		PreparedStatement prps=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, getScore);
			prps.setString(2, userID);
			
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
		
	}

	
}
