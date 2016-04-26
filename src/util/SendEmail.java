package util;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.security.auth.Subject;
import javax.activation.*;

public class SendEmail {
	//
	public static void send(String to, String subject, String text) {
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
		final String username = "traveltheworldaround8@gmail.com";//
		final String password = "1T23ravel8";
		try {
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// -- Create a new message --
			Message msg = new MimeMessage(session);

			// -- Set the FROM and TO fields --
			msg.setFrom(new InternetAddress("traveltheworldaround8@gmail.com"));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			msg.setSubject(subject);
			msg.setText(text);
			msg.setSentDate(new Date());
			Transport.send(msg);
			System.out.println("Message sent.");
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Erreur d'envoi, cause: " + e);

		}
	}

	public static void send(String from, String to, String host, String subject, String text) {
		System.out.println("SendMail (" + subject + ") \nfrom: " + from + "\nto: " + to + "\ntext: " + text);
		// Recipient's email ID needs to be mentioned.
		// String to = "vasic.marko@mail.ru";

//		 Sender's email ID needs to be mentioned
//		 String from = "marko921@hotmail.com";

		// Assuming you are sending email from localhost
		// String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			System.out.println("Create a default MimeMessage object.....");
			MimeMessage message = new MimeMessage(session);

			System.out.println("Set From: header field of the header.....");
			message.setFrom(new InternetAddress(from));

			System.out.println("Set To: header field of the header.....");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			System.out.println("Set Subject: header field....");
			message.setSubject(subject);

			System.out.println("Now set the actual message....");
			message.setText(text);

			System.out.println("Send message....");
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}