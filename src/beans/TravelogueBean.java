package beans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.sun.mail.handlers.message_rfc822;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.RateEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import dao.CommentDAO;
import dao.MessageDAO;
import dao.RatingDAO;
import dao.TravelogueDAO;
import dao.UserDAO;
import dto.Comment;
import dto.Message;
import dto.Travelogue;
import util.SendEmail;

public class TravelogueBean {
	private Travelogue travelogue = new Travelogue();
	private Comment comment = new Comment();
	private String search = new String();
	private String category = new String();
	private Integer rating;

	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Travelogue> getTravelogues() {
		if (category.equals("my")) {
			return TravelogueDAO.traveloguesNameAndAuthor(search, userBean.getUser().getUsername());
		} else if (category.equals("unviewed")) {
			return TravelogueDAO.allUnviewdTravelogues(search);
		} else if (category.equals("unapproved")) {
			return TravelogueDAO.allUnapprovedTravelogues(search);
		} else {
			return TravelogueDAO.allTravelogues(search);
		}
	}

	public Travelogue getTravelogue() {
		System.out.println("getTravelogue");
		rating = RatingDAO.ratingForTravelogue(travelogue.getId(), userBean.getUser().getUsername());
		return travelogue;
	}

	public void setTravelogue(Travelogue travelogue) {
		System.out.println("setTravelogue");
		rating = RatingDAO.ratingForTravelogue(travelogue.getId(), userBean.getUser().getUsername());
		this.travelogue = travelogue;
	}

	public Comment getComment() {
		System.out.println("getComment");
		return comment;
	}

	public void setComment(Comment comment) {
		System.out.println("setComment");
		this.comment = comment;
	}

	public boolean isTravelogueEditable() {
		return travelogue.getAuthor().equals(userBean.getUser().getUsername());
	}

	public String viewTravelogue() {
		System.out.println("*** viewTravelogue " + travelogue.getId() + " name =" + travelogue.getName());
		return "travelogueView.xhtml?id="+travelogue.getId();
	}

	public String editTravelogue() {
		if (isTravelogueEditable()) {
			return "addTravelogues.xhtml?id=" + travelogue.getId();
		} else {
			return viewTravelogue();
		}
	}

	private void redirectToTravelogueView() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		try {
			externalContext.redirect(
					externalContext.getRequestContextPath() + "/faces/travelogueView.xhtml?id=" + travelogue.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public void approve() {
		TravelogueDAO.approve(travelogue.getId());
		String eMail = UserDAO.selectByUsername(travelogue.getAuthor()).geteMail();
		System.out.println("eMail = "+ eMail);
		SendEmail.send(eMail, "Notification", "Travelogue '" + travelogue.getName() + "' was approved!");
		MessageDAO.insert(new Message(-1, new Date(), "Travelogue '" + travelogue.getName() + "' was approved!", false,
				userBean.getUser().getUsername(), travelogue.getAuthor()));
		addMessage("Notification", "Travelogue '" + travelogue.getName() + "' was approved!");
	}

	public void unapprove() {
		TravelogueDAO.unapprove(travelogue.getId());
		String eMail = UserDAO.selectByUsername(travelogue.getAuthor()).geteMail();
		System.out.println("eMail = "+ eMail);
		SendEmail.send(eMail, "Notification", "Travelogue '" + travelogue.getName() + "' wasn't approved!");
		MessageDAO.insert(new Message(-1, new Date(), "Travelogue '" + travelogue.getName() + "' wasn't approved!",
				false, userBean.getUser().getUsername(), travelogue.getAuthor()));
		addMessage("Notification", "Travelogue '" + travelogue.getName() + "' wasn't approved!");
	}

	public void saveComment() {
		System.out.println("*** saveComment text = "+comment.getText());
		comment.setAuthor(userBean.getUser().getUsername());
		comment.setDate(new Date());
		CommentDAO.insertForTravelogue(comment, travelogue.getId());
		comment = new Comment();
//		return viewTravelogue();
	}

	public void saveTravelogue() {
		if (travelogue.getName() != null && travelogue.getDate() != null && travelogue.getText() != null) {
			travelogue.setAuthor(userBean.getUser().getUsername());
			if (TravelogueDAO.getTravelogue(travelogue.getId()) == null) {
				if (TravelogueDAO.insert(travelogue)) {
					addMessage( "Succesful ", travelogue.getName() + " is uploaded.");
//					FacesMessage message = new FacesMessage("Succesful ", travelogue.getName() + " is uploaded.");
//					FacesContext.getCurrentInstance().addMessage(null, message);
					System.out.println("new Travelogue");
					travelogue = new Travelogue();
				}
			} else {
				if (TravelogueDAO.update(travelogue)) {
					addMessage( "Succesful ", travelogue.getName() + " is uploaded.");
//					FacesMessage message = new FacesMessage("Succesful ", travelogue.getName() + " is uploaded.");
//					FacesContext.getCurrentInstance().addMessage(null, message);
					System.out.println("new Travelogue");
					travelogue = new Travelogue();
				}
			}

		} else {
			addMessage( "Error ", travelogue.getName() + " isn't uploaded.");
		}
	}

	public void onrate(RateEvent rateEvent) {
		System.out.println("onrate " + rating);

		RatingDAO.insertRatingForTravelogue(travelogue.getId(), userBean.getUser().getUsername(), rating);
		RatingDAO.updateRatingForTravelogue(travelogue.getId(), userBean.getUser().getUsername(), rating);
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event",
					"You rated:" + ((Integer) rateEvent.getRating()).intValue());			

		FacesContext.getCurrentInstance().addMessage(null, message);
		// redirectToTravelogueView();
	}

	public void oncancel() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");
		FacesContext.getCurrentInstance().addMessage(null, message);
		// redirectToTravelogueView();
	}

	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public void preRenderTravelogueView(ComponentSystemEvent event) throws IOException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		System.out.println("preRenderTravelogueView");
		String id = request.getParameter("id");
		String message = "Error:%20";

		if (id != null) {
			try {
				int travelogueId = Integer.parseInt(id);
				travelogue = TravelogueDAO.getTravelogue(travelogueId);
			} catch (NumberFormatException nfe) {
				message += "travelogue%20with%20id%20" + id + "%20doesn't%20exist";
			}
		} else {
			message += "you%20must%20select%20travelogue%20which%20you%20want%20to%20view";
		}

		if (travelogue.getId() == -1) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext
					.redirect(externalContext.getRequestContextPath() + "/faces/errorPage.xhtml?message=" + message);
		} else {
			travelogue = TravelogueDAO.getTravelogue(travelogue.getId());
		}
	}

	public void preRenderTravelogueAdd(ComponentSystemEvent event) throws IOException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		System.out.println("preRenderTravelogueView");
		String id = request.getParameter("id");
		String message = "Error:%20";
		travelogue = new Travelogue();
		if (id != null) {
			try {
				int travelogueId = Integer.parseInt(id);
				travelogue = TravelogueDAO.getTravelogue(travelogueId);
			} catch (NumberFormatException nfe) {
			}
		}
	}

	public String getFacebookShare() {
		return "<div>" + "<div id=\"fb-root\"></div>" + "<script>" + "(function(d, s, id) {"
				+ "var js, fjs = d.getElementsByTagName(s)[0];" + "if (d.getElementById(id))" + "return;"
				+ "js = d.createElement(s);" + "js.id = id;"
				+ "js.src = \"//connect.facebook.net/sr_RS/sdk.js#xfbml=1&version=v2.6\";"
				+ "fjs.parentNode.insertBefore(js, fjs);" + "}(document, 'script', 'facebook-jssdk'));" + "</script>"
				+ "<div class=\"fb-share-button\""
				+ "data-href=\"http://localhost:8080/TravelTheWorldAround/faces/travelogueView.xhtml?id="
				+ travelogue.getId() + "\"" + "data-layout=\"button_count\" data-mobile-iframe=\"true\"></div>"
				+ "</div>";
	}

	public StreamedContent getTraveloguePDF() throws DocumentException, IOException {
		// step 1
		Document document = new Document();
		System.out.println("step 1!");
		// step 2
		System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRealPath("images"));

		File tmp = new File(
				FacesContext.getCurrentInstance().getExternalContext().getRealPath("images") + "\\" + "travelogue.pdf");
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(tmp));
		System.out.println("step 2!");
		// step 3
		document.open();
		System.out.println("step 3!");
		// step 4
		String str = travelogue.getText();
		InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
		System.out.println("step 4!");
		XMLWorkerHelper.getInstance().parseXHtml(writer, document,
				is);
//		new FileInputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath("index.html"));
		System.out.println("step 5!");
		// step 5
		document.close();
		System.out.println("PDF Created!");

		InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
				.getResourceAsStream("/images/travelogue.pdf");
		StreamedContent file;
		file = new DefaultStreamedContent(stream, "application/pdf", travelogue.getName()+".pdf");

		return file;
	}
}
