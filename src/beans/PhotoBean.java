package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.RateEvent;
import org.primefaces.model.UploadedFile;

import dao.CommentDAO;
import dao.MessageDAO;
import dao.PhotoDAO;
import dao.RatingDAO;
import dao.TravelogueDAO;
import dao.UserDAO;
import dto.Comment;
import dto.Message;
import dto.Photo;
import util.SendEmail;

public class PhotoBean {
	private UploadedFile file;
	private Photo photo = new Photo();
	private Comment comment = new Comment();
	private Integer rating;
	
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;
	
	@ManagedProperty(value = "#{travelogueBean}")
	private TravelogueBean travelogueBean;
	
	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	public TravelogueBean getTravelogueBean() {
		return travelogueBean;
	}

	public void setTravelogueBean(TravelogueBean travelogueBean) {
		this.travelogueBean = travelogueBean;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public void addMessage(String summary, String detail) {
		System.out.println("****** addMessage " + detail);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	public void onrate(RateEvent rateEvent) {
		System.out.println("onrate " + rating);

		RatingDAO.insertRatingForPhoto(photo.getId(), userBean.getUser().getUsername(), rating);//(travelogue.getId(), userBean.getUser().getUsername(), rating);
		RatingDAO.updateRatingForPhoto(photo.getId(), userBean.getUser().getUsername(), rating); //Travelogue(travelogue.getId(), userBean.getUser().getUsername(), rating);
		
		addMessage("Rate Event", "You rated:" + ((Integer) rateEvent.getRating()).intValue());
//		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event",
//					"You rated:" + ((Integer) rateEvent.getRating()).intValue());			
//
//		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void oncancel() {
		addMessage("Cancel Event", "Rate Reset");
//		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");
//		FacesContext.getCurrentInstance().addMessage(null, message);
		// redirectToTravelogueView();
	}
	public UploadedFile getFile() {
		System.out.println("getFile");
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
		System.out.println("setFile");
	}

	public Photo getPhoto() {
		System.out.println("getPhoto");
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
		System.out.println("setPhoto");
	}

	public Comment getComment() {
		System.out.println("getComment");
		return comment;
	}

	public void setComment(Comment comment) {
		System.out.println("setComment");
		this.comment = comment;
	}

	public String getPictureName() {
		String name = "no-image.png";
		if (file != null) {
			name = file.getFileName();
		}
		return name;
	}

	public List<Photo> getPhotos() {
		return PhotoDAO.allPhotos(travelogueBean.getTravelogue().getId());
	}
	
	public void saveComment() {
		System.out.println("saveComment " + comment.getText());
		comment.setAuthor(userBean.getUser().getUsername());
		comment.setDate(new Date());
		System.out.println("*******aveComment " + CommentDAO.insertForPhoto(comment, photo.getId()));
		comment = new Comment();
//		return "photos.xhtml";
	}
	
	public void upload() {
		System.out.println("upload1");
		if (file != null) {
			System.out.println("upload succes");
			File tmp = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("images") + "\\"
					+ file.getFileName());
			try {
				InputStream is = file.getInputstream();
				OutputStream os = new FileOutputStream(tmp);

				byte[] buff = new byte[1024];
				int length;

				while ((length = is.read(buff)) > 0) {
					os.write(buff, 0, length);
				}

				is.close();
				os.close();

				//TODO
				photo.setAuthor("vaske");
				photo.setPath(file.getFileName());

				if (PhotoDAO.insert(photo, travelogueBean.getTravelogue().getId())) {
					FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
					FacesContext.getCurrentInstance().addMessage(null, message);
					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("slika");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("upload error");
			FacesMessage message = new FacesMessage("File isn't uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRealPath("images"));
	}
	public void preRenderPhotoView(ComponentSystemEvent event) throws IOException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		System.out.println("preRenderPhotoView");
		String id = request.getParameter("id");
		String message = "Error:%20";

		if (id != null) {
			try {
				int photoId = Integer.parseInt(id);
				photo = PhotoDAO.getPhoto(photoId);
			} catch (NumberFormatException nfe) {
				message += "travelogue%20with%20id%20" + id + "%20doesn't%20exist";
			}
		} else {
			message += "you%20must%20select%20travelogue%20which%20you%20want%20to%20view";
		}

		if (photo.getId() == -1) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext
					.redirect(externalContext.getRequestContextPath() + "/faces/errorPage.xhtml?message=" + message);
		} else {
			photo = PhotoDAO.getPhoto(photo.getId());
		}
	}
	
	public void approve() {
		addMessage("Notification", "approved");

		PhotoDAO.approve(photo.getId());
		String eMail = UserDAO.selectByUsername(photo.getAuthor()).geteMail();
		System.out.println("eMail = "+ eMail);
		SendEmail.send(eMail, "Notification", "Photo '" + photo.getName() + "' was approved!");
		MessageDAO.insert(new Message(-1, new Date(), "Photo '" + photo.getName() + "' was approved!", false,
				userBean.getUser().getUsername(), photo.getAuthor()));
	}

	public void unapprove() {
		addMessage("Notification", "unapproved");

		PhotoDAO.unapprove(photo.getId());
		String eMail = UserDAO.selectByUsername(photo.getAuthor()).geteMail();
		System.out.println("eMail = "+ eMail);
		SendEmail.send(eMail, "Notification", "Photo '" + photo.getName() + "' wasn't approved!");
		MessageDAO.insert(new Message(-1, new Date(), "Photo '" + photo.getName() + "' wasn't approved!",
				false, userBean.getUser().getUsername(), photo.getAuthor()));
	}
	
}
