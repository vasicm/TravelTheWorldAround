package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dto.Comment;
import dto.Photo;
import dto.Travelogue;

public class PhotoDAO {
private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	
	private static final String SQL_INSERT = "INSERT INTO `travel_the_world_around`.`photo` (`name`, `path`, `user_username`, `travelogue_id`) VALUES (?, ?, ?, ?);";
	private static final String SQL_SELECT_ALL = "SELECT * FROM photo where photo.travelogue_id = ?";
	private static final String SQL_SELECT_PHOTO = "SELECT * FROM photo where photo.id = ?";
	
	private static final String SQL_APPROVE = "UPDATE `travel_the_world_around`.`photo` SET `state`='1' WHERE `id`= ?";
	private static final String SQL_UNAPPROVE = "UPDATE `travel_the_world_around`.`photo` SET `state`='-1' WHERE `id`= ?";
	
	public static boolean approve(int photoId) {
		Object values[] = {photoId};
		return DAOUtil.executeUpdate(values, SQL_APPROVE);
	}
	public static boolean unapprove(int photoId) {
		Object values[] = {photoId};
		return DAOUtil.executeUpdate(values, SQL_UNAPPROVE);
	}
	
	public static boolean insert(Photo photo, int travelogueId) {
		boolean retVal = false;
		Connection connection = null;
		ResultSet generatedKeys = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Object values[] = {photo.getName(), photo.getPath(), photo.getAuthor(), travelogueId};
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
	
	public static Photo getPhoto(int id) {
		Photo photo = null;
		Connection connection = null;
		ResultSet rs = null;
		Object values[] = {id};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_PHOTO, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()){
				photo = new Photo(
						id,
						rs.getString("name"),
						rs.getString("path"),
						rs.getString("user_username"),
						rs.getInt("state"),
						RatingDAO.averageRatingForPhoto(id),
						CommentDAO.allPhotoComment(id)
				);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return photo;
	}
	
	public static List<Photo> allPhotos(int travelogueId) {
		List<Photo> photos = new ArrayList<Photo>();
		Connection connection = null;
		ResultSet rs = null;
		Photo photo = null;
		Object values[] = {travelogueId};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_ALL, false, values);
			rs = pstmt.executeQuery();
			while (rs.next()){
				int id = rs.getInt("id");
				photo = new Photo(
						id,
						rs.getString("name"),
						rs.getString("path"),
						rs.getString("user_username"),
						rs.getInt("state"),
						RatingDAO.averageRatingForPhoto(id),
						CommentDAO.allPhotoComment(id)
				);
				photos.add(photo);
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return photos;
	}
}
