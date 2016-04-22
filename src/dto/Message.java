package dto;

import java.util.Date;

public class Message {
	private int id;
	private Date sendDate;
	private String text;
	private boolean seen;
	private String from;
	private String to;
	public Message() {
		super();
	}
	public Message(int id, Date sendDate, String text, boolean seen, String from, String to) {
		super();
		this.id = id;
		this.sendDate = sendDate;
		this.text = text;
		this.seen = seen;
		this.from = from;
		this.to = to;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
}
