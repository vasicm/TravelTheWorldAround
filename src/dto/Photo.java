package dto;

import java.util.List;

public class Photo {
	private int id;
	private String name;
	private String path;
	private String author;
	private int state;
	private float rating;
	private List<Comment> comments;
	public Photo() {
		super();
		id = -1;
	}
	public Photo(int id, String name, String path, String author, int state, float rating, List<Comment> comments) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
		this.author = author;
		this.state = state;
		this.rating = rating;
		this.comments = comments;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public int getRatingInteger() {
		return Math.round(rating);
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
}
