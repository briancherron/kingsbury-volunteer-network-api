package edu.kingsbury.task_tracker.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import edu.kingsbury.task_tracker.user.User;

public class TaskInvitation {

	protected static void send(long taskId, String taskName, User user) throws FileNotFoundException, IOException, EmailException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(System.getProperty("catalina.home") + File.separator + "/conf/kvn.properties"));
		
		HtmlEmail email = new HtmlEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		email.setAuthentication(properties.getProperty("email"), properties.getProperty("password"));
		email.setSSLOnConnect(true);
		email.setFrom(properties.getProperty("email"));
		email.addTo(user.getEmail());
		email.setSubject("You're invited: " + taskName);
		StringBuilder message = new StringBuilder();
		message.append("<html><body>");
		message.append("<p>" + user.getFirstName() + ",</p>");
		message.append("<p>You've been invited to volunteer for the following task:<br><br>");
		message.append("<p>" + taskName + "</p><br><br>");
		message.append("<p>To participate, click <a href='" + properties.getProperty("taskUrl") + taskId  + "'>here</a>.<br><br>");
		message.append("<p>Thanks,<br>Kingsbury Volunteer Network</p>");
		message.append("</body></html>");
		email.setMsg(message.toString());
		
		email.send();
	}
}
