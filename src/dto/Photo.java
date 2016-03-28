package dto;

import java.util.List;

public class Photo {
	private int id;
	private String name;
	private String path;
	private List<Comment> comments;
	public Photo() {
		super();
	}
	public Photo(int id, String name, String path, List<Comment> comments) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
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
}
