package miniproject.roomdao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import miniproject.catchmind.waitingRoomRCreateDTO;
import miniproject.membership.dao.MembershipDAO;

public class RoomDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.80.1:1521:xe";
	private String user = "catch";
	private String password = "catch";
	
	
	private static RoomDAO instance;	
	public static RoomDAO getInstance() {
		if(instance==null) {
			synchronized (RoomDAO.class) {
				instance = new RoomDAO();
			}
		}
		return instance;
	}	
	
	public RoomDAO() {
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
	
	public int getSeq() {
		int seq = 0;
		Connection conn = null;
		PreparedStatement prps = null;		
		ResultSet rs = null;
		
		String sql = "select seq_room.nextval from dual";
			
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

	public void insertArticle(waitingRoomRCreateDTO waitingroomrcreateDTO) {
		String sql = "insert into room_info values(?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement prps = null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, waitingroomrcreateDTO.getRoomSeq());
			prps.setString(2, waitingroomrcreateDTO.getRoomName());
			prps.setString(3, waitingroomrcreateDTO.getRoomPass());
			prps.setInt(4, waitingroomrcreateDTO.getCurrentPerson());
			prps.setInt(5, waitingroomrcreateDTO.getLimitPerson());
			
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

	public boolean isRoomExist(String roomTitle) {
		boolean roomTitleCheck=false;
		String sql = "select * from room_info where roomtitle = ?";
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		int sw=0;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setString(1, roomTitle);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				int limit = rs.getInt("limitperson");
				//limitperson의 default는 2
				//사용하지 않을경우 0으로 세팅
				if(limit==0) sw=0;//사용가능
				else {
					sw=1; //중복한 방이 존재
					return roomTitleCheck;//사용 불가
				}
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
		if(sw==0) roomTitleCheck=true;		
		
		return roomTitleCheck;
	}

	public void plusPerson(waitingRoomRCreateDTO waitingroomrcreateDTO) {
		String sql = "update room_info set currentperson ="
				+ " currentperson+1 where seq = ?";		
		
		Connection conn=null;
		PreparedStatement prps=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, waitingroomrcreateDTO.getRoomSeq());
			
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

	public int getCurrentPerson(waitingRoomRCreateDTO waitingroomrcreateDTO) {
		int currentPerson=0;
		String sql = "select * from room_info where seq = ? and roomtitle = ?";
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, waitingroomrcreateDTO.getRoomSeq());
			prps.setString(2, waitingroomrcreateDTO.getRoomName());
			rs = prps.executeQuery();
			
			if(rs.next()) {
				currentPerson = rs.getInt("currentperson");				
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
		return currentPerson;
	}

	public void minusPerson(waitingRoomRCreateDTO waitingroomrcreateDTO, int roomSeq) {
		String sql = "update room_info set currentperson = currentperson-1 where seq = ?";		
		
		Connection conn=null;
		PreparedStatement prps=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, roomSeq);
			
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

	public void initLimitPerson(waitingRoomRCreateDTO waitingroomrcreateDTO, int roomSeq) {
		String sql = "update room_info set limitperson =? where seq = ?";		
		
		Connection conn=null;
		PreparedStatement prps=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, 0);
			prps.setInt(2, roomSeq);
			
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

	public int getAvailableRoom() {
		int availableRoom=0;
		String sql = "select * from room_info";
		Connection conn=null;
		PreparedStatement prps=null;
		ResultSet rs=null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			rs = prps.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("currentperson")>0) availableRoom++;				
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
		return availableRoom;
	}

	
	
}
