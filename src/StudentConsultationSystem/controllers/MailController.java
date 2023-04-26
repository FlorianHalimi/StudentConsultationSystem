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
    private Properties mailServerProperties;
    private Session getMailSession;
    private MimeMessage generateMailMessage;
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
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");

            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            generateMailMessage.setSubject(" Konsultime | " + subject);
//            emailBody += "<br><br>`${crrUser}` <br> <a href='https://motyim.github.io/voidChat/'>visit us</a>";
//            generateMailMessage.setContent(emailBody, "text/html");


            Transport transport = getMailSession.getTransport("smtp");
            transport.connect(SMTP, SENDER_MAIL, PASSWORD);
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
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
