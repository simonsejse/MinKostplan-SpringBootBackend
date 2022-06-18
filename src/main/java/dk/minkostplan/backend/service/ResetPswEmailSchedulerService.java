package dk.minkostplan.backend.service;

import dk.minkostplan.backend.config.SmtpMailSender;
import dk.minkostplan.backend.models.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@EnableScheduling
public class ResetPswEmailSchedulerService {
    public final SmtpMailSender mailSender;
    @Autowired
    public ResetPswEmailSchedulerService(SmtpMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Scheduled(fixedRate = 15000)
    public void dequeueAndSend() throws MessagingException {
        if (mailSender.getEmailRequestQueue().isEmpty())
            return;

        EmailRequest req = mailSender.getEmailRequestQueue().poll();

        mailSender.send(req.getTo(), req.getSubject(), req.getBody());
    }
}
