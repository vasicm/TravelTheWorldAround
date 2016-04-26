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

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import dto.Comment;
import dto.Travelogue;
import dto.User;

public class TravelogueDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String SQL_INSERT = "INSERT INTO `travel_the_world_around`.`travelogue` (`name`, `date`, `locationInfo`, `text`, `user_username`) VALUES (?, ?, ?, ?, ?);";
	private static final String SQL_SELECT_ALL = "SELECT * FROM travelogue";
	private static final String SQL_SELECT_ALL_ORDER_SHARES ="select * from travelogue order by shares desc";
	private static final String SQL_SELECT_ID = "SELECT * FROM travelogue where id = ?";
	private static final String SQL_UPDATE = "UPDATE `travel_the_world_around`.`travelogue` SET `name`=?, `date`=?, `locationInfo`=?, `text`=?, `state`='0' WHERE `id`=?";
	private static final String SQL_DELETE = "DELETE FROM `travel_the_world_around`.`travelogue` WHERE `id`=?";
	private static final String SQL_TRAVELOGUE_BY_NAME = "select * from travelogue where travelogue.name like ? and travelogue.state = 1";
	
	private static final String SQL_TRAVELOGUE_BY_NAME_AND_AUTHOR = "select * from travelogue where travelogue.name like ? and travelogue.user_username like ?";
	private static final String SQL_UNVIEWED_TRAVELOGUE_BY_NAME = "select * from travelogue where travelogue.name like ? and travelogue.state = 0";
	private static final String SQL_UNAPPROVED_TRAVELOGUE_BY_NAME = "select * from travelogue where travelogue.name like ? and travelogue.state = -1";
	
	private static final String SQL_APPROVE = "UPDATE `travel_the_world_around`.`travelogue` SET `state`='1' WHERE `id`= ?";
	private static final String SQL_UNAPPROVE = "UPDATE `travel_the_world_around`.`travelogue` SET `state`='-1' WHERE `id`= ?";
	
	private static final String SQL_SELECT_COUNT_APPROVED = "select count(*) as cou from travelogue where state = 1";
	private static final String SQL_SELECT_COUNT_UNAPPROVED = "select count(*) as cou from travelogue where state = -1";
	
	public static boolean approve(int travelogueId) {
		Object values[] = {travelogueId};
		return DAOUtil.executeUpdate(values, SQL_APPROVE);
	}
	public static boolean unapprove(int travelogueId) {
		Object values[] = {travelogueId};
		return DAOUtil.executeUpdate(values, SQL_UNAPPROVE);
	}
	
	public static boolean insert(Travelogue travelogue) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Object values[] = {travelogue.getName(), df.format(travelogue.getDate()), travelogue.getLocationInfo(), travelogue.getText(), travelogue.getAuthor()};
		return DAOUtil.executeUpdate(values, SQL_INSERT);
//		boolean retVal = false;
//		Connection connection = null;
//		ResultSet generatedKeys = null;
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		Object values[] = {travelogue.getName(), df.format(travelogue.getDate()), travelogue.getLocationInfo(), travelogue.getText(), travelogue.getAuthor()};
//		try {
//			connection = connectionPool.checkOut();
//			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_INSERT, true,
//					values);
//			int affectedRows = pstmt.executeUpdate();
//			if (affectedRows == 0)
//				retVal = false;
//			else
//				retVal = true;
//			pstmt.close();
//		} catch (SQLException e) {
//			retVal = false;
//		} finally {
//			connectionPool.checkIn(connection);
//		}
//		return retVal;
	}
	
	public static boolean update(Travelogue travelogue) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Object values[] = {travelogue.getName(), df.format(travelogue.getDate()), travelogue.getLocationInfo(), travelogue.getText(), travelogue.getId()};
		return DAOUtil.executeUpdate(values, SQL_UPDATE);
//		boolean retVal = false;
//		Connection connection = null;
//		ResultSet generatedKeys = null;
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		Object values[] = {travelogue.getName(), df.format(travelogue.getDate()), travelogue.getLocationInfo(), travelogue.getText(), travelogue.getId()};
//		try {
//			connection = connectionPool.checkOut();
//			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_UPDATE, true,
//					values);
//			int affectedRows = pstmt.executeUpdate();
//			if (affectedRows == 0)
//				retVal = false;
//			else
//				retVal = true;
//			pstmt.close();
//		} catch (SQLException e) {
//			retVal = false;
//		} finally {
//			connectionPool.checkIn(connection);
//		}
//		return retVal;
	}
	
	public static boolean delete(int id) {
		Object values[] = {id};
		return DAOUtil.executeUpdate(values, SQL_DELETE);
//		boolean retVal = false;
//		Connection connection = null;
//		ResultSet generatedKeys = null;
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		Object values[] = {id};
//		try {
//			connection = connectionPool.checkOut();
//			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_DELETE, true,
//					values);
//			int affectedRows = pstmt.executeUpdate();
//			if (affectedRows == 0)
//				retVal = false;
//			else
//				retVal = true;
//			pstmt.close();
//		} catch (SQLException e) {
//			retVal = false;
//		} finally {
//			connectionPool.checkIn(connection);
//		}
//		return retVal;
	}
	
	public static List<Travelogue> allTraveloguesOrderShares() {
		Object values[] = {};
		return getTravelogue(values, SQL_SELECT_ALL_ORDER_SHARES);
	}
	
	public static List<Travelogue> allTravelogues() {
		Object values[] = {};
		return getTravelogue(values, SQL_SELECT_ALL);
//		List<Travelogue> travelogues = new ArrayList<Travelogue>();
//		Connection connection = null;
//		ResultSet rs = null;
//		Travelogue trav = null;
//		Object values[] = {};
//		try {
//			connection = connectionPool.checkOut();
//			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
//					SQL_SELECT_ALL, false, values);
//			rs = pstmt.executeQuery();
//			while (rs.next()){
//				String text = rs.getString("text");
//				String[] textSp = text.split("/p>", 4);
//				if(textSp.length == 4) {
//					text = textSp[0] + "/p>" + textSp[1] + "/p>" + textSp[2] + "/p>";
//					text = Jsoup.clean(text, Whitelist.relaxed());
//				}
//				int id = rs.getInt("id");
//				trav = new Travelogue(
//						id, 
//						rs.getString("name"), 
//						rs.getDate("date"),
//						rs.getString("locationInfo"), 
//						text, 
//						rs.getString("user_username"),
//						rs.getInt("state"),
//						RatingDAO.averageRatingForTravelogue(id),
//						rs.getInt("shares"),
//						null,
//						null
//				);
//				travelogues.add(trav);
//			}
//			pstmt.close();
//		} catch (SQLException exp) {
//			exp.printStackTrace();
//		} finally {
//			connectionPool.checkIn(connection);
//		}
//		return travelogues;
	}
	
	public static List<Travelogue> allTravelogues(String name) {
		name = "%"+name+"%";
		Object values[] = {name};
		return getTravelogue(values, SQL_TRAVELOGUE_BY_NAME);
		
//		List<Travelogue> travelogues = new ArrayList<Travelogue>();
//		Connection connection = null;
//		ResultSet rs = null;
//		Travelogue trav = null;
//		name = "%"+name+"%";
//		Object values[] = {name};
//		try {
//			connection = connectionPool.checkOut();
//			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
//					SQL_TRAVELOGUE_BY_NAME, false, values);
//			rs = pstmt.executeQuery();
//			while (rs.next()){
//				String text = rs.getString("text");
//				String[] textSp = text.split("/p>", 4);
//				if(textSp.length == 4) {
//					text = textSp[0] + "/p>" + textSp[1] + "/p>" + textSp[2] + "/p>";
//					text = Jsoup.clean(text, Whitelist.relaxed());
//				}
//				int id = rs.getInt("id");
//				trav = new Travelogue(
//						id, 
//						rs.getString("name"), 
//						rs.getDate("date"),
//						rs.getString("locationInfo"), 
//						text, 
//						rs.getString("user_username"),
//						rs.getInt("state"),
//						RatingDAO.averageRatingForTravelogue(id),
//						rs.getInt("shares"),
//						null,
//						null
//				);
//				travelogues.add(trav);
//			}
//			pstmt.close();
//		} catch (SQLException exp) {
//			exp.printStackTrace();
//		} finally {
//			connectionPool.checkIn(connection);
//		}
//		return travelogues;
	}
	
	public static Travelogue getTravelogue(int id) {
		Connection connection = null;
		ResultSet rs = null;
		Travelogue travelogue = null;
		Object values[] = {id};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_ID, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()){
				String text = rs.getString("text");
				text = Jsoup.clean(text, Whitelist.relaxed());
				travelogue = new Travelogue(
						id, 
						rs.getString("name"), 
						rs.getDate("date"),
						rs.getString("locationInfo"), 
						text, 
						rs.getString("user_username"),
						rs.getInt("state"),
						RatingDAO.averageRatingForTravelogue(id),
						rs.getInt("shares"),
						PhotoDAO.allPhotos(id),
						CommentDAO.allTravelogueComment(id)
				);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return travelogue;
	}
	
	public static List<Travelogue> traveloguesNameAndAuthor(String name, String author) {
		name = "%"+name+"%";
		Object values[] = {name, author};
		return getTravelogue(values, SQL_TRAVELOGUE_BY_NAME_AND_AUTHOR);
		
//		List<Travelogue> travelogues = new ArrayList<Travelogue>();
//		Connection connection = null;
//		ResultSet rs = null;
//		Travelogue trav = null;
//		name = "%"+name+"%";
//		Object values[] = {name, author};
//		try {
//			connection = connectionPool.checkOut();
//			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
//					SQL_TRAVELOGUE_BY_NAME_AND_AUTHOR, false, values);
//			rs = pstmt.executeQuery();
//			while (rs.next()){
//				String text = rs.getString("text");
//				String[] textSp = text.split("/p>", 4);
//				if(textSp.length == 4) {
//					text = textSp[0] + "/p>" + textSp[1] + "/p>" + textSp[2] + "/p>";
//					text = Jsoup.clean(text, Whitelist.relaxed());
//				}
//				int id = rs.getInt("id");
//				trav = new Travelogue(
//						id, 
//						rs.getString("name"), 
//						rs.getDate("date"),
//						rs.getString("locationInfo"), 
//						text, 
//						rs.getString("user_username"),
//						rs.getInt("state"),
//						RatingDAO.averageRatingForTravelogue(id),
//						rs.getInt("shares"),
//						null,
//						null
//				);
//				travelogues.add(trav);
//			}
//			pstmt.close();
//		} catch (SQLException exp) {
//			exp.printStackTrace();
//		} finally {
//			connectionPool.checkIn(connection);
//		}
//		return travelogues;
	}
	
	public static List<Travelogue> allUnviewdTravelogues(String name) {
		name = "%"+name+"%";
		Object values[] = {name};
		return getTravelogue(values, SQL_UNVIEWED_TRAVELOGUE_BY_NAME);
		
//		List<Travelogue> travelogues = new ArrayList<Travelogue>();
//		Connection connection = null;
//		ResultSet rs = null;
//		Travelogue trav = null;
//		name = "%"+name+"%";
//		Object values[] = {name};
//		try {
//			connection = connectionPool.checkOut();
//			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
//					SQL_UNVIEWED_TRAVELOGUE_BY_NAME, false, values);
//			rs = pstmt.executeQuery();
//			while (rs.next()){
//				String text = rs.getString("text");
//				String[] textSp = text.split("/p>", 4);
//				if(textSp.length == 4) {
//					text = textSp[0] + "/p>" + textSp[1] + "/p>" + textSp[2] + "/p>";
//					text = Jsoup.clean(text, Whitelist.relaxed());
//				}
//				int id = rs.getInt("id");
//				trav = new Travelogue(
//						id, 
//						rs.getString("name"), 
//						rs.getDate("date"),
//						rs.getString("locationInfo"), 
//						text, 
//						rs.getString("user_username"),
//						rs.getInt("state"),
//						RatingDAO.averageRatingForTravelogue(id),
//						rs.getInt("shares"),
//						null,
//						null
//				);
//				travelogues.add(trav);
//			}
//			pstmt.close();
//		} catch (SQLException exp) {
//			exp.printStackTrace();
//		} finally {
//			connectionPool.checkIn(connection);
//		}
//		return travelogues;
	}
	
	public static List<Travelogue> allUnapprovedTravelogues(String name) {
		name = "%"+name+"%";
		Object values[] = {name};
		return getTravelogue(values, SQL_UNAPPROVED_TRAVELOGUE_BY_NAME);
//		List<Travelogue> travelogues = new ArrayList<Travelogue>();
//		Connection connection = null;
//		ResultSet rs = null;
//		Travelogue trav = null;
//		name = "%"+name+"%";
//		Object values[] = {name};
//		try {
//			connection = connectionPool.checkOut();
//			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
//					SQL_UNAPPROVED_TRAVELOGUE_BY_NAME, false, values);
//			rs = pstmt.executeQuery();
//			while (rs.next()){
//				String text = rs.getString("text");
//				String[] textSp = text.split("/p>", 4);
//				if(textSp.length == 4) {
//					text = textSp[0] + "/p>" + textSp[1] + "/p>" + textSp[2] + "/p>";
//					text = Jsoup.clean(text, Whitelist.relaxed());
//				}
//				int id = rs.getInt("id");
//				trav = new Travelogue(
//						id, 
//						rs.getString("name"), 
//						rs.getDate("date"),
//						rs.getString("locationInfo"), 
//						text, 
//						rs.getString("user_username"),
//						rs.getInt("state"),
//						RatingDAO.averageRatingForTravelogue(id),
//						rs.getInt("shares"),
//						null,
//						null
//				);
//				travelogues.add(trav);
//			}
//			pstmt.close();
//		} catch (SQLException exp) {
//			exp.printStackTrace();
//		} finally {
//			connectionPool.checkIn(connection);
//		}
//		return travelogues;
	}
	
	public static int approvedConunt() {
		Connection connection = null;
		ResultSet rs = null;
		int count = 0;
		Object values[] = {};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_COUNT_APPROVED, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()){
				count = rs.getInt("cou");
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return count;
	}
	
	public static int unapprovedConunt() {
		Connection connection = null;
		ResultSet rs = null;
		int count = 0;
		Object values[] = {};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_COUNT_UNAPPROVED, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()){
				count = rs.getInt("cou");
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return count;
	}
	
	public static List<Travelogue> getTravelogue(Object values[], String SQL) {
		List<Travelogue> travelogues = new ArrayList<Travelogue>();
		Connection connection = null;
		ResultSet rs = null;
		Travelogue trav = null;
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()){
				String text = rs.getString("text");
				String[] textSp = text.split("/p>", 4);
				if(textSp.length == 4) {
					text = textSp[0] + "/p>" + textSp[1] + "/p>" + textSp[2] + "/p>";
					text = Jsoup.clean(text, Whitelist.relaxed());
				}
				int id = rs.getInt("id");
				trav = new Travelogue(
						id, 
						rs.getString("name"), 
						rs.getDate("date"),
						rs.getString("locationInfo"), 
						text, 
						rs.getString("user_username"),
						rs.getInt("state"),
						RatingDAO.averageRatingForTravelogue(id),
						rs.getInt("shares"),
						null,
						null
				);
				travelogues.add(trav);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return travelogues;
	}
}
