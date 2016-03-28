package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import dto.User;


public class UserDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	private static final String SQL_SELECT_BY_USERNAME_AND_PASSWORD = "SELECT * FROM korisnik WHERE korisnicko_ime=? AND lozinka=?";
	
	private static String getGroup(int group) {
		String str = new String("reg");
		if(group == 2) {
			str = "admin";
		}
		
		return str;
	}
	
	private static String makeDate(String date) {
		String[] dateSplit = date.split("/");
		String ret = new String(dateSplit[2] + dateSplit[1] + dateSplit[0]);
		
		return ret;
	}
	
	private static final String SQL_INSERT = "INSERT INTO korisnik (korisnicko_ime, lozinka, ime, prezime, e_mail, kratka_biografija, datum_rodjenja, grupa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static boolean insert(User user) {
		boolean retVal = false;
		Connection connection = null;
		ResultSet generatedKeys = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Object values[] = { user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.geteMail(), user.getBio(), df.format(user.getBrDate()), (user.getGroup().equals("admin")?"2":"1")};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_INSERT, true,
					values);
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
	
	public static User selectByUsernameAndPassword(String username, String password){
		User user = null;
		Connection connection = null;
		ResultSet rs = null;
		Object values[] = {username, password};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_BY_USERNAME_AND_PASSWORD, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()){
				user = new User(
						rs.getString("korisnicko_ime"), 
						rs.getString("lozinka"), 
						rs.getString("ime"),
						rs.getString("prezime"), 
						rs.getString("e_mail"), 
						rs.getString("kratka_biografija"), 
						rs.getDate("datum_rodjenja"), 
						getGroup(rs.getInt("grupa"))
				);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return user;
	}

}
