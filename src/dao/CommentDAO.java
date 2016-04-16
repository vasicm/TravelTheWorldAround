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

import dto.Comment;
import dto.Travelogue;
import dto.User;

public class CommentDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	
	private static final String SQL_SELECT_ALL_COMMENT_TRAV = "select * from comment where travelogue_id = ?";
	private static final String SQL_INSERT_TRAVELOGUE_COMMENT = "INSERT INTO `travel_the_world_around`.`comment` (`text`, `date`, `travelogue_id`, `user_username`) VALUES (?, ?, ?, ?)";
	
	public static boolean insertForTravelogue(Comment comm, int travelogueId) {
		boolean retVal = false;
		Connection connection = null;
		ResultSet generatedKeys = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Object values[] = {comm.getText(), df.format(comm.getDate()), travelogueId,  comm.getAuthor()};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_INSERT_TRAVELOGUE_COMMENT, true,
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

	public static List<Comment> allTravelogueComment(int travelogueId) {
		List<Comment> comments = new ArrayList<Comment>();
		Connection connection = null;
		ResultSet rs = null;
		Comment comm = null;
		Object values[] = {travelogueId};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_ALL_COMMENT_TRAV, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()){
				comm = new Comment(
						rs.getInt("id"), 
						rs.getString("text"), 
						rs.getDate("date"), 
						rs.getString("user_username")
				);
				comments.add(comm);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return comments;
	}
}
