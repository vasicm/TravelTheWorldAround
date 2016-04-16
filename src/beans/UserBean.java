package beans;

import dto.User;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import dao.UserDAO;

public class UserBean {
	//TODO
	private User user = new User("vaske", "1234", "", "", "", "", null,"reg");
	private boolean loggedIn = true;
	
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

	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public boolean isAdministrator() {
		return (user.getGroup().equals("admin"));
	}
	
	public void checkLoggedIn(ComponentSystemEvent event) throws IOException {
		System.out.println("init");
	    if (!loggedIn) {
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	        externalContext.redirect(externalContext.getRequestContextPath() + "/faces/login.xhtml");
	    }
	}
}
