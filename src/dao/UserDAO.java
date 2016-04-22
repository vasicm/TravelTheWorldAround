package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import dto.Travelogue;
import dto.User;

public class UserDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String SQL_SELECT_BY_USERNAME_AND_PASSWORD = "SELECT * FROM user "
			+ "WHERE username=? AND password=?";

	private static final String SQL_INSERT = "INSERT INTO user "
			+ "(username, password, name, surname, e_mail, bio, br_date, group) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_ALL_USERS_IN_CONTACT = "select user.* from user, contact "
			+ "where user.username = contact.user_username2 and contact.user_username1 = ?";

	private static final String SQL_ALL_USERS_NOT_IN_CONTACT = "select * from user "
			+ "where user.username != ? and user.username like ? and user.username "
			+ "not in (select user.username from user, " + "contact where user.username = contact.user_username2 "
			+ "and contact.user_username1 = ? " + ")";

	private static final String SQL_INSERT_CONTACT = "INSERT INTO `travel_the_world_around`.`contact` "
			+ "(`user_username1`, `user_username2`) " + "VALUES (?, ?)";

	private static String getGroup(int group) {
		String str = new String("reg");
		if (group == 2) {
			str = "admin";
		}

		return str;
	}

	private static String makeDate(String date) {
		String[] dateSplit = date.split("/");
		String ret = new String(dateSplit[2] + dateSplit[1] + dateSplit[0]);

		return ret;
	}

	public static boolean insertContact(String username1, String username2) {
		System.out.println("insertContact");
		boolean retVal = false;
		Connection connection = null;
		ResultSet generatedKeys = null;
		Object values[] = { username1, username2 };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_INSERT_CONTACT, true, values);
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

	public static boolean insert(User user) {
		boolean retVal = false;
		Connection connection = null;
		ResultSet generatedKeys = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Object values[] = { user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.geteMail(),
				user.getBio(), df.format(user.getBrDate()), (user.getGroup().equals("admin") ? "2" : "1") };
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

	public static User selectByUsernameAndPassword(String username, String password) {
		User user = null;
		Connection connection = null;
		ResultSet rs = null;
		Object values[] = { username, password };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_SELECT_BY_USERNAME_AND_PASSWORD, false,
					values);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new User(rs.getString("username"), rs.getString("password"), rs.getString("name"),
						rs.getString("surname"), rs.getString("e_mail"), rs.getString("bio"), rs.getDate("br_date"),
						getGroup(rs.getInt("group")));
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return user;
	}

	public static List<User> allUsersInContact(String username) {
		System.out.println("allUsersInContact");
		List<User> users = new ArrayList<User>();
		Connection connection = null;
		ResultSet rs = null;
		User user = null;
		Object values[] = { username };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_ALL_USERS_IN_CONTACT, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new User(rs.getString("username"), "", rs.getString("name"), rs.getString("surname"),
						rs.getString("e_mail"), rs.getString("bio"), rs.getDate("br_date"),
						getGroup(rs.getInt("group")));
				users.add(user);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return users;
	}

	public static List<User> allUsersNotInContact(String username, String search) {
		System.out.println("allUsersNotInContact");
		List<User> users = new ArrayList<User>();
		Connection connection = null;
		ResultSet rs = null;
		User user = null;
		Object values[] = { username, "%"+search+"%", username };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_ALL_USERS_NOT_IN_CONTACT, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = new User(rs.getString("username"), "", rs.getString("name"), rs.getString("surname"),
						rs.getString("e_mail"), rs.getString("bio"), rs.getDate("br_date"),
						getGroup(rs.getInt("group")));
				users.add(user);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return users;
	}
}
