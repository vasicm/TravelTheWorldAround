package beans;

public class Flight {
	private String from;
	private String to;
	private int price;
	
	public Flight() {
		super();
	}
	public Flight(String from, String to, int price) {
		super();
		this.from = from;
		this.to = to;
		this.price = price;
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
