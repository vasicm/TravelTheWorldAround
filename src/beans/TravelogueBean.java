package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import dao.CommentDAO;
import dao.TravelogueDAO;
import dto.Comment;
import dto.Travelogue;

public class TravelogueBean {
	private Travelogue travelogue = new Travelogue();
	private Comment comment = new Comment();
	
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public List<Travelogue> getTravelogues() {
		return TravelogueDAO.allTravelogues();
	}

	public Travelogue getTravelogue() {
		return travelogue;
	}

	public void setTravelogue(Travelogue travelogue) {
		this.travelogue = travelogue;
	}
	
	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public String viewTravelogue() {
		System.out.println("*** viewTravelogue " + travelogue.getName());
		travelogue = TravelogueDAO.getTravelogue(travelogue.getId());
		return "travelogueView.xhtml";
	}
	
	public String saveComment() {
		comment.setAuthor(userBean.getUser().getUsername());
		comment.setDate(new Date());
		CommentDAO.insertForTravelogue(comment, travelogue.getId());
		comment = new Comment();
		return viewTravelogue();
	}
	
	public void saveTravelogue() {
		if(travelogue.getName() != null && travelogue.getDate() != null && travelogue.getText() != null) {
			travelogue.setAuthor(userBean.getUser().getUsername());
			if(TravelogueDAO.getTravelogue(travelogue.getId()) == null) {
				if(TravelogueDAO.insert(travelogue)) {
					FacesMessage message = new FacesMessage("Succesful ", travelogue.getName() + " is uploaded.");
					FacesContext.getCurrentInstance().addMessage(null, message);
					System.out.println("new Travelogue");
					travelogue = new Travelogue();
				}
			}else {
				if(TravelogueDAO.update(travelogue)) {
					FacesMessage message = new FacesMessage("Succesful ", travelogue.getName() + " is uploaded.");
					FacesContext.getCurrentInstance().addMessage(null, message);
					System.out.println("new Travelogue");
					travelogue = new Travelogue();
				}
			}
			
		} else {
			FacesMessage message = new FacesMessage("sdfdsfdsfdsfsdf.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
}
