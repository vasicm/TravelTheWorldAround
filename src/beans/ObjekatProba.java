package beans;

import java.util.Iterator;
import java.util.Queue;

public class ObjekatProba {
	private Queue<String> red;

	public ObjekatProba(Queue<String> red) {
		super();
		this.red = red;
	}
	
	public void add(String str) {
		red.add(str);
	}
	
	public void print() {
		Iterator<String> iter = red.iterator();
		System.out.println("print:");
		while(iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
}
