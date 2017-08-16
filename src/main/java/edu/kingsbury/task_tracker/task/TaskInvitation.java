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
		message.append("<html><body style='font-family: sans-serif;'>");
		message.append("<p style='margin-bottom: 1em;'>" + user.getFirstName() + ",</p>");
		message.append("<p style='margin-bottom: 1em;'>You've been invited to volunteer for the following task:");
		message.append("<p style='margin-bottom: 1em;'>" + taskName + "</p>");
		message.append("<p style='margin-bottom: 1em;'>To participate, click <a href='" + properties.getProperty("taskUrl") + taskId  + "'>here</a>.");
		message.append("<p style='margin-bottom: 1em;'>Thanks,</p>");
		message.append("<p style='margin-bottom: 1em;'>Kingsbury Community Volunteer Network</p>");
		message.append("</body></html>");
		email.setMsg(message.toString());
		
		email.send();
	}
}
