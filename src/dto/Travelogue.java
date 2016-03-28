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
	private List<Comment> comments;
	public Travelogue() {
		super();
	}
	public Travelogue(int id, String name, Date date, String locationInfo, String text, String author,
			List<Comment> comments) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
		this.locationInfo = locationInfo;
		this.text = text;
		this.author = author;
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
		comments.add(comm);
	}
}
