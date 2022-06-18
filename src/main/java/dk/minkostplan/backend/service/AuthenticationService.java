package dk.minkostplan.backend.service;

import dk.minkostplan.backend.config.SmtpMailSender;
import dk.minkostplan.backend.entities.PasswordToken;
import dk.minkostplan.backend.entities.User;
import dk.minkostplan.backend.models.MailValidator;
import dk.minkostplan.backend.payload.response.PwdResetTokenView;
import dk.minkostplan.backend.utils.Messaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.StringTokenizer;

@Service("authenticationService")
public class AuthenticationService {

    private final UserService userService;
    private final SmtpMailSender mailSender;

    @Autowired
    public AuthenticationService(UserService userService, SmtpMailSender mailSender){
        this.userService = userService;
        this.mailSender = mailSender;
    }

    /**
     *
     * @param email the user email address
     * @param req the HttpServletRequest autowired by spring context
     * @return
     *  It ALWAYS returns a ResponseEntity with HttpStatus OK no matter what!
     *  This is to prevent possible TIME ATTACK assaults, where users can send a request and determine
     *  if a user exists in the database by noticing time delays.
     *  When we do this method, we queue and dequeue mails, but instantly send a reponse of OK back.
     *  So the users sending the request won't notice any time delay, but instead just get instant OK in return
     *  Therefore, he won't know if users exists or not.
     *  Or scheduler running on a separate thread will make sure mails are sent!
     *
     */
    @Transactional
    public String createResetCredentialsRequest(String email, HttpServletRequest req){
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()){
            PasswordToken passwordToken = new PasswordToken(user.get());
            req.setAttribute("RESET_PWD_CODE", new PwdResetTokenView(passwordToken.getUniqueId(), passwordToken.getExpiresAt(), email));
            String url = "http://www.localhost:3000/cred/reset?passwordToken=" + passwordToken.getUniqueId();
            String resetCredentials = Messaging.RESET_CREDENTIALS_HTML.replace("${url}", url);
            mailSender.queue(MailValidator.VALID, email, "Reset your credentials!", resetCredentials);
        }else {
            mailSender.queue(MailValidator.FAKE, email, "POSSIBLE TIME ATTACK!", String.format("IP Address %s tried to reset password with following email %s", getClientIpAddress(req), email));
        }
        return "OK";
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }


}
