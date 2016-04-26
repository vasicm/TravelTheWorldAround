package beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class FlightsBean {
	private Date departing;
	private Date returning;

	public Date getDeparting() {
		return departing;
	}

	public void setDeparting(Date departing) {
		this.departing = departing;
	}

	public Date getReturning() {
		return returning;
	}

	public void setReturning(Date returning) {
		this.returning = returning;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/CheapFlights/").build();
	}

	public List<Flight> getFlights() {
		List<Flight> flights = new ArrayList<Flight>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		departing = new Date();
		returning = departing;
		
		String response = service.path("flights").path(df.format(departing)).path(df.format(returning))
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		
		try {
			JSONObject json = new JSONObject(response);
			JSONArray arr = json.getJSONArray("flight");
			for(int i=0;i<arr.length();i++) {
				String from = arr.getJSONObject(i).getString("from");
				String to = arr.getJSONObject(i).getString("to");
				int price = arr.getJSONObject(i).getInt("price");
				flights.add(new Flight(from, to, price));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		flights.add(new Flight("Beograd", "Zagreb", 123));
		return flights;
	}
}
