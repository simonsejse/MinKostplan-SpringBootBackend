package dk.minkostplan.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class SmtpMailSender {
    private static final String HTML_SIGNATURE = "<div><div><br></div><div>Med venlig hilsen,<br></div><div>Simon<br></div><div>Udvikler<br></div><div><br></div><div><img style=\"border-radius:5px;\" src=\"https://i.imgur.com/FC4dhyt.png\" alt=\"picture\"><br></div><div>www.min-kostplan.dk<br></div></div>";

    @Autowired
    private final JavaMailSender javaMailSender;

    public SmtpMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(String to, String subject, String body) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;

        helper = new MimeMessageHelper(message, false); // true indicates
        // multipart message
        helper.setSubject(subject);
        helper.setFrom("no-reply@min-kostplan.dk");
        helper.setTo(to);

        helper.setText(body + "<br />" + HTML_SIGNATURE, true); // true indicates html

        javaMailSender.send(message);

    }


    //@Bean
    void sendMailBean() throws MessagingException {
        StringBuilder message = new StringBuilder();

        message.append("Hej Tugge!")
                .append("<br/>");

        message.append("Tak for din mail, du er en løvebror!")
                .append("<br/>");


        send("lazesmash@gmail.com", "RE: Efterspørgsmål", message.toString());
    }


}