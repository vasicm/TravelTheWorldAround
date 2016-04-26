package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Photo;

public class RatingDAO {
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String SQL_INSERT_RATING_FOR_TRAVELOGUE = "INSERT INTO `travel_the_world_around`.`ratingTravelogue` "
			+ "(`user_username`, `travelogue_id`, `rating`) "
			+ "VALUES (?, ?, ?)";
	private static final String SQL_INSERT_RATING_FOR_PHOTO = "INSERT INTO `travel_the_world_around`.`ratingPhoto` "
			+ "(`user_username`, `photo_id`, `rating`) "
			+ "VALUES (?, ?, ?);";
	
	private static final String SQL_UPDATE_RATING_FOR_TRAVELOGUE = "UPDATE `travel_the_world_around`.`ratingTravelogue` "
			+ "SET `rating`=? WHERE `user_username`=? and`travelogue_id`=?";
	private static final String SQL_UPDATE_RATING_FOR_PHOTO = "UPDATE `travel_the_world_around`.`ratingPhoto` "
			+ "SET `rating`=? WHERE `user_username`=? and`photo_id`=?";
	
	
	private static final String SQL_SELECT_AVERAGE_RATING_FOR_TRAVELOGUE = "select avg(ratingtravelogue.rating) as average "
			+ "from ratingTravelogue where ratingTravelogue.travelogue_id = ?";
	
	private static final String SQL_SELECT_AVERAGE_RATING_FOR_PHOTO ="select avg(ratingphoto.rating) as average "
			+ "from ratingphoto where ratingphoto.photo_id = ?";
	
	
	private static final String SQL_SELECT_RATING_FOR_TRAVELOGUE = "select ratingtravelogue.rating from ratingtravelogue "
			+ "where ratingtravelogue.travelogue_id = ? and ratingtravelogue.user_username = ?";
	
	private static final String SQL_SELECT_RATING_FOR_PHOTO = "select ratingphoto.rating from ratingphoto "
			+ "where ratingphoto.photo_id = ? and ratingphoto.user_username = ?";

	public static boolean insertRatingForTravelogue(int travelogueId, String username, int rating) {
		Object values[] = {username, travelogueId, rating};
		return DAOUtil.executeUpdate(values, SQL_INSERT_RATING_FOR_TRAVELOGUE);
	}
	
	public static boolean updateRatingForTravelogue(int travelogueId, String username, int rating) {
		Object values[] = {rating, username, travelogueId};
		return DAOUtil.executeUpdate(values, SQL_UPDATE_RATING_FOR_TRAVELOGUE);
	}
	
	public static boolean insertRatingForPhoto(int photoId, String username, int rating) {
		Object values[] = {username, photoId, rating};
		return DAOUtil.executeUpdate(values, SQL_INSERT_RATING_FOR_PHOTO);
	}
	
	public static boolean updateRatingForPhoto(int photoId, String username, int rating) {
		Object values[] = {rating, username, photoId};
		return DAOUtil.executeUpdate(values, SQL_UPDATE_RATING_FOR_PHOTO);
	}
	
	public static float averageRatingForTravelogue(int travelogueId) {
		float average = 0;
		Connection connection = null;
		ResultSet rs = null;
		Object values[] = {travelogueId};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_AVERAGE_RATING_FOR_TRAVELOGUE, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()){
				average = rs.getFloat("average");
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		
		return average;
	}
	
	public static int ratingForTravelogue(int travelogueId, String username) {
		int rating = -1;
		Connection connection = null;
		ResultSet rs = null;
		Object values[] = {travelogueId, username};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_RATING_FOR_TRAVELOGUE, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()){
				rating = rs.getInt("rating");
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return rating;
	}
	
	public static float averageRatingForPhoto(int photoId) {
		float average = 0;
		Connection connection = null;
		ResultSet rs = null;
		Object values[] = {photoId};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_AVERAGE_RATING_FOR_PHOTO, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()){
				average = rs.getFloat("average");
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		
		return average;
	}
	
	public static int ratingForPhoto(int photoId, String username) {
		int rating = -1;
		Connection connection = null;
		ResultSet rs = null;
		Object values[] = {photoId, username};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection,
					SQL_SELECT_RATING_FOR_PHOTO, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()){
				rating = rs.getInt("rating");
			}
			pstmt.close();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return rating;
	}
}
