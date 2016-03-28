package dto;

public class Comment {
	private int id;
	private String text;
	private String author;
	
	
	public Comment() {
		super();
	}


	public Comment(int id, String text, String author) {
		super();
		this.id = id;
		this.text = text;
		this.author = author;
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
