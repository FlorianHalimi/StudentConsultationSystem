package StudentConsultationSystem.controllers;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailController {
    private Properties properties;
    private Session session;
    private MimeMessage message;
    final private String SENDER_MAIL = "tt202fdfd@gmail.com";
    final private String PASSWORD = "pssbeatczyvxrhij";
    final private String SMTP = "smtp.gmail.com";
    private String to;
    private String subject;
    private String emailBody;

    public MailController(String to, String subject, String emailBody) {
        this.to = to;
        this.subject = subject;
        this.emailBody = emailBody;
    }

    public boolean sendMail() {
        try {
            properties = System.getProperties();
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            session = Session.getDefaultInstance(properties, null);
            message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(" Konsultime | " + subject);
            emailBody += "<br><br>Cdo te mire! <br>";
            message.setContent(emailBody, "text/html");


            Transport transport = session.getTransport("smtp");
            transport.connect(SMTP, SENDER_MAIL, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (AddressException ex) {
            ex.printStackTrace();
            return false;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;

    }

}
