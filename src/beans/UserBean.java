package beans;

import dto.Message;
import dto.User;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import dao.MessageDAO;
import dao.UserDAO;

public class UserBean {
	//TODO
	private User user = new User("vaske", "1234", "", "", "", "", null,"reg");
	private User selectedUser = new User();
	private String messageText = new String();
	private String search = new String();
	
	private boolean loggedIn = true;
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String login() {
		System.out.println("\n\n*************login****************\n\n");
		if ((user = UserDAO.selectByUsernameAndPassword(user.getUsername(), user.getPassword()))!=null) {
			loggedIn = true;
			return "index?faces-redirect=true";
		}
		user = new User();
		loggedIn = false;
//		TODO delete this
//		System.out.println("\n\n\nTEST\n\n\n");
//		ArrayDeque<String> tmp = new ArrayDeque<String>();
//		tmp.add("element1");
//		ObjekatProba ob1 = new ObjekatProba(tmp);
//		ObjekatProba ob2 = new ObjekatProba(tmp);
//		tmp.add("element2");
//		ob1.add("element3");
//		ob1.print();
//		ob2.add("element4");
//		ob1.print();
//		ob2.print();
//		System.out.println("\n\n\nTEST END\n\n\n");
		//
		return "login?faces-redirect=true";
	}
	
	public String logout() {
		user = new User();
		loggedIn = false;
		return "login?faces-redirect=true";
	}

	public String signUp() {
		System.out.println("\n\n*************signUp****************\n\n");
		UserDAO.insert(user);
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
	
	public void checkLoggedIn(ComponentSystemEvent event) throws IOException {
		System.out.println("checkLoggedIn");
	    if (!loggedIn) {
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	        externalContext.redirect(externalContext.getRequestContextPath() + "/faces/login.xhtml");
	    }
	}
	
	public List<User> getAllUsersInContact() {
		return UserDAO.allUsersInContact(user.getUsername());
	}
	
	public List<User> getAllUsersNotInContact() {
		return UserDAO.allUsersNotInContact(user.getUsername(), search);
	}
	
	public List<Message> getAllMessage() {
		return MessageDAO.allMessages(user.getUsername(), selectedUser.getUsername());
	}
	
	public String messages() {
		return "messages.xhtml";
	}
	
	public String sendMessage() {
		MessageDAO.insert(new Message(-1, new Date(), messageText, false, user.getUsername(), selectedUser.getUsername()));
		messageText = "";
		return "message.xhtml";
	}
	
	public String addContact() {
		System.out.println("addContact");
		UserDAO.insertContact(user.getUsername(), selectedUser.getUsername());
		UserDAO.insertContact(selectedUser.getUsername(), user.getUsername());
		return "contact.xhtml";
	}
}
