package beans;

import dto.Message;
import dto.Travelogue;
import dto.User;
import util.SendEmail;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import dao.MessageDAO;
import dao.TravelogueDAO;
import dao.UserDAO;

public class UserBean {

	private User user = new User();
	private User selectedUser = new User();
	private String messageText = new String();
	private String search = new String();

	private boolean loggedIn = false;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String login() {
		System.out.println("\n\n*************login****************\n\n");
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(user.getPassword().getBytes());
			if ((user = UserDAO.selectByUsernameAndPassword(user.getUsername(), new String(md.digest()))) != null) {
				loggedIn = true;
				return "index?faces-redirect=true";
			} else {
				user = new User();
				loggedIn = false;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			user = new User();
			loggedIn = false;
		}
		return "login?faces-redirect=true";
	}

	public String loginAsGuest() {
		user = new User("guest", null, null, null, null, null, null, "guest");
		loggedIn = true;
		return "index?faces-redirect=true";
	}

	public String logout() {
		user = new User();
		loggedIn = false;
		return "login?faces-redirect=true";
	}

	public String signUp() {
		System.out.println("\n\n*************signUp****************\n\n" + user.getUsername());
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(user.getPassword().getBytes());
			user.setPassword(new String(md.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		System.out.println("insert = " + UserDAO.insert(user));
		return "index?faces-redirect=true";
	}

	public User getUser() {
		return user;
	}

	public User getSelectedUser() {
		System.out.println("addContact");
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public boolean isAdministrator() {
		return (user.getGroup().equals("admin"));
	}

	public boolean isRegisteredUser() {
		return (user.getGroup().equals("reg") || user.getGroup().equals("admin"));
	}

	public void checkLoggedIn(ComponentSystemEvent event) throws IOException {
		System.out.println("checkLoggedIn");

		if (!loggedIn) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect(externalContext.getRequestContextPath() + "/faces/login.xhtml");
		}
	}
	
	public void checkMessage(ComponentSystemEvent event) throws IOException {
		System.out.println("checkMessage");

		if (getMessagesCount() > 0) {
			String from = MessageDAO.messagesFrom(user.getUsername());
			selectedUser = UserDAO.selectByUsername(from);
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect(externalContext.getRequestContextPath() + "/faces/messagesView.xhtml");
		}
	}

	public void checkLoggedInAsRegisteredUser(ComponentSystemEvent event) throws IOException {
		System.out.println("checkLoggedInAsRegisteredUser");

		if (!isRegisteredUser()) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect(externalContext.getRequestContextPath() + "/faces/login.xhtml");
		}
	}

	public void checkLoggedInAsAdministrator(ComponentSystemEvent event) throws IOException {
		System.out.println("checkLoggedInAsAdministrator");

		if (!isAdministrator()) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect(externalContext.getRequestContextPath() + "/faces/login.xhtml");
		}
	}

	public List<User> getAllUnviewedUsers() {
		return UserDAO.allUnvieweddUsers();
	}

	public List<User> getAllUnapprovedUsers() {
		return UserDAO.allUnapprovedUsers();
	}

	public List<User> getAllUsersInContact() {
		return UserDAO.allUsersInContact(user.getUsername());
	}

	public List<User> getAllUsersNotInContact() {
		return UserDAO.allUsersNotInContact(user.getUsername(), search);
	}

	public int getMessagesCount() {
		return MessageDAO.messagesCount(user.getUsername());
	}

	public List<Message> getAllMessage() {
		return MessageDAO.allMessages(user.getUsername(), selectedUser.getUsername());
	}

	public String messages() {
		return "messagesView.xhtml";
	}

	public String approve() {
		System.out.println(
				"****** " + selectedUser.getUsername() + "approve = " + UserDAO.approve(selectedUser.getUsername()));
		String eMail = selectedUser.geteMail();
		System.out.println("eMail = " + eMail);
		SendEmail.send(eMail, "Notification", "Welcome!");
		return "contact.xhtml";
	}

	public String unapprove() {
		System.out.println(
				"****** " + selectedUser.getUsername() + "approve = " + UserDAO.unapprove(selectedUser.getUsername()));
		String eMail = selectedUser.geteMail();
		System.out.println("eMail = " + eMail);
		SendEmail.send(eMail, "Notification", "Welcome!");
		return "contact.xhtml";
	}

	public String sendMessage() {
		MessageDAO.insert(
				new Message(-1, new Date(), messageText, false, user.getUsername(), selectedUser.getUsername()));
		messageText = "";
		return "messageView.xhtml";
	}

	public String addContact() {
		System.out.println("addContact");
		UserDAO.insertContact(user.getUsername(), selectedUser.getUsername());
		UserDAO.insertContact(selectedUser.getUsername(), user.getUsername());
		return "contact.xhtml";
	}

	public String getReportHTML() {
		String ret =  "<html>" 
				+ "<head>" 
				+ "<title>Insert title here</title>"
				+ "</head>" 
				+ "<body>"
				+ "	<p style=\"font:italic bold 20px Georgia, serif\">Three most shared travelogues:</p>";
		List<Travelogue> travelogues = TravelogueDAO.allTraveloguesOrderShares();
		
		for(int i=0; i< travelogues.size() && i< 3; i++){
			ret	+= "<p style=\"font:italic bold 20px Georgia, serif\">" +travelogues.get(i).getName()+"</p><br />";
			ret	+= "<div>" +travelogues.get(i).getText()+"</div><hr />";
		}
		
		ret	+= "<hr />";
			
		ret	+= "	<p style=\"font:italic bold 20px Georgia, serif\">Number of approved travelogues: " + TravelogueDAO.approvedConunt() + "</p><hr />"
				+ "	<p style=\"font:italic bold 20px Georgia, serif\">Number of unapproved travelogues: " + TravelogueDAO.unapprovedConunt() +"</p><hr />"
				+ "</body>" 
				+ "</html>";
		return ret;
	}
}
