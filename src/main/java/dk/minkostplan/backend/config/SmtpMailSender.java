package dk.minkostplan.backend.config;

import dk.minkostplan.backend.models.EmailRequest;
import dk.minkostplan.backend.models.MailValidator;
import dk.minkostplan.backend.utils.Messaging;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@Component
public class SmtpMailSender {
    private static final Logger log = Logger.getLogger(SmtpMailSender.class.getName());

    @Getter
    private Queue<EmailRequest> emailRequestQueue = new ConcurrentLinkedQueue<>();

    private final JavaMailSender javaMailSender;

    @Autowired
    public SmtpMailSender(JavaMailSender javaMailSender){
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

        helper.setText(body + "<br />" + Messaging.HTML_SIGNATURE, true); // true indicates html

        javaMailSender.send(message);

    }

    private void queue(String to, String subject, String body){
        EmailRequest emailRequest = new EmailRequest(to, subject, body);
        this.emailRequestQueue.add(emailRequest);
    }

    public void queue(MailValidator validator, String to, String subject, String body){
        switch(validator){
            case FAKE:
                queue("no-reply@min-kostplan.dk", subject, body);
                log.warning(String.format("%s:%s:%s", to, subject, body));
                break;
            case VALID:
                queue(to, subject, body);
                break;
        }
    }


}