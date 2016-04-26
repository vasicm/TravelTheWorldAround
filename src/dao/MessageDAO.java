package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dto.Message;
import dto.Travelogue;
import dto.User;

public class MessageDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	
	private static final String SQL_MESSAGES = "select * from message "
			+ "where message.from = ? "
			+ "and message.to = ? or "
			+ "message.from = ? "
			+ "and message.to = ? "
			+ "order by message.send_date";
	
	private static final String SQL_MESSAGES_COUNT = "select count(*) as cou from message "
			+ "where message.to = ? "
			+ "and message.seen = 0";
	
	private static final String SQL_MESSAGES_FROM = "select message.from from message "
			+ "where message.to = ?"
			+ "and message.seen = 0";
	
	private static final String SQL_MESSAGES_COUNT_FROM = "select count(*) as cou from message "
			+ "where message.to = ? and message.from = ? "
			+ "and message.seen = 0";
	
	private static final String SQL_INSERT = "INSERT INTO `travel_the_world_around`.`message` "
			+ "(`send_date`, `text`, `seen`, `from`, `to`) "
			+ "VALUES (?, ?, ?, ?, ?)";
	
	private static final String SQL_UPDATE = "UPDATE `travel_the_world_around`.`message` "
			+ "SET "
			+ "`send_date`=?, "
			+ "`text`=?, "
			+ "`seen`=?, "
			+ "`from`=?, "
			+ "`to`=? "
			+ "WHERE `id`=?";
	
	public static int messagesCount(String userName) {
		System.out.println("messagesCount");
		int ret = 0;
		Connection connection = null;
		ResultSet rs = null;
		Message message = null;
		Object values[] = {userName};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_MESSAGES_COUNT, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()){
				ret = rs.getInt("cou");
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return ret;
	}
	
	public static String messagesFrom(String userName) {
		System.out.println("messagesCount");
		String from = "";
		Connection connection = null;
		ResultSet rs = null;
		Message message = null;
		Object values[] = {userName};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_MESSAGES_FROM, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()){
				from = rs.getString("from");
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return from;
	}
	
	public static List<Message> allMessages(String userName1, String userName2) {
		System.out.println("allMessages");
		List<Message> messages = new ArrayList<Message>();
		Connection connection = null;
		ResultSet rs = null;
		Message message = null;
		Object values[] = {userName1, userName2, userName2, userName1};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_MESSAGES, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()){
				message = new Message(
					rs.getInt("id"), 
					rs.getDate("send_date"), 
					rs.getString("text"), 
					rs.getBoolean("seen"), 
					rs.getString("from"), 
					rs.getString("to")
				);
				if(message.getFrom().equals(userName2) && !message.isSeen()) {
					message.setSeen(true);
					update(message);
				}
				messages.add(message);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return messages;
	}
	
	public static boolean update(Message message) {
		System.out.println("update");
		boolean retVal = false;
		Connection connection = null;
		ResultSet generatedKeys = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Object values[] = { 
			df.format(message.getSendDate()), 
			message.getText(), 
			message.isSeen(), 
			message.getFrom(), 
			message.getTo(),
			message.getId()
		};
		
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_UPDATE, true, values);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0)
				retVal = false;
			else
				retVal = true;
			pstmt.close();
		} catch (SQLException e) {
			retVal = false;
		} finally {
			connectionPool.checkIn(connection);
		}
		return retVal;
	}
	
	public static boolean insert(Message message) {
		System.out.println("insert");
		boolean retVal = false;
		Connection connection = null;
		ResultSet generatedKeys = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Object values[] = { 
			df.format(message.getSendDate()), 
			message.getText(), 
			message.isSeen(), 
			message.getFrom(), 
			message.getTo()
		};
		
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_INSERT, true, values);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0)
				retVal = false;
			else
				retVal = true;
			pstmt.close();
		} catch (SQLException e) {
			retVal = false;
		} finally {
			connectionPool.checkIn(connection);
		}
		return retVal;
	}
}
