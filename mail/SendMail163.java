import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail163 {

	public static void main(String[] args) {

		final String username = "m13641930976";
		final String password = "";

		// http://help.163.com/10/0731/11/6CTUBPT300753VB8.html
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		 props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("m13641930976@163.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("m13641930976@163.com"));
			message.setSubject("Test");
			message.setText("Test!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			
			System.out.println("It might be a firewall issue, try 'telnet smtp.163.com 25'");
			throw new RuntimeException(e);
		}
	}
}