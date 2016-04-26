package dto;

import java.util.Date;

public class Comment {
	private int id;
	private String text;
	private Date date;
	private String author;
	
	public Comment() {
		super();
		System.out.println("Comment::Comment");
		text = "";
	}
	public Comment(int id, String text, Date date, String author) {
		super();
		System.out.println("Comment::Comment text = "+ text);
		this.id = id;
		this.text = text;
		this.date = date;
		this.author = author;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
}
