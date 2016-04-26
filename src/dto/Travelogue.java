package dto;

import java.util.Date;
import java.util.List;


public class Travelogue {
	private int id;
	private String name;
	private Date date;
	private String locationInfo;
	private String text;
	private String author;
	private int state;
	private float rating;
	private int shares;
	private List<Photo> photos;
	private List<Comment> comments;
	public Travelogue() {
		super();
		this.id = -1;
	}
	

	public Travelogue(int id, String name, Date date, String locationInfo, String text, String author, int state,
			float rating, int shares, List<Photo> photos, List<Comment> comments) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
		this.locationInfo = locationInfo;
		this.text = text;
		this.author = author;
		this.state = state;
		this.rating = rating;
		this.shares = shares;
		this.photos = photos;
		this.comments = comments;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLocationInfo() {
		return locationInfo;
	}
	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public void addComment(Comment comm) {
		if(comments != null) {
			comments.add(comm);
		}
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public float getRating() {
		return rating;
	}
	public int getRatingInteger() {
		return Math.round(rating);
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	
}
