package dk.minkostplan.backend.service;

import dk.minkostplan.backend.config.SmtpMailSender;
import dk.minkostplan.backend.entities.PasswordToken;
import dk.minkostplan.backend.entities.User;
import dk.minkostplan.backend.exceptions.ResetCredentialsException;
import dk.minkostplan.backend.models.MailValidator;
import dk.minkostplan.backend.payload.request.ResetCredentialsRequest;
import dk.minkostplan.backend.payload.response.PwdResetTokenView;
import dk.minkostplan.backend.utils.Messaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.StringTokenizer;

@Service("authenticationService")
public class AuthenticationService {

    private final UserService userService;
    private final SmtpMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserService userService, SmtpMailSender mailSender, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
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
            System.out.println("TEST");
            req.getSession().setAttribute("RESET_PWD_CODE", new PwdResetTokenView(passwordToken.getUniqueId(), passwordToken.getExpiresAt(), email));
            System.out.println(req.getSession().getId()+":id");
            System.out.println("TEST");
            String url = "http://localhost:3000/reset/credentials?passwordToken=" + passwordToken.getUniqueId();
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

    @Transactional
    public String resetCredentials(ResetCredentialsRequest resetCredentials, HttpServletRequest req) throws ResetCredentialsException {
        System.out.println("TEST2");
        System.out.println(req.getSession().getId()+":id");
        System.out.println("TEST2");
        PwdResetTokenView pwdResetTokenView = (PwdResetTokenView) req.getSession().getAttribute("RESET_PWD_CODE");
        if (pwdResetTokenView == null){
            throw new ResetCredentialsException("Du har ikke anmodet om at resætte dit kodeord!", HttpStatus.NOT_FOUND);
        }

        if (!resetCredentials.getToken().equals(pwdResetTokenView.getToken().toString())){
            throw new ResetCredentialsException("Token didn't match token in session!", HttpStatus.BAD_REQUEST);
        }

        if (LocalDateTime.now().isAfter(pwdResetTokenView.getExpiresAt())){
            throw new ResetCredentialsException("Din token er udløbet! Den udløber efter 15minutter!", HttpStatus.REQUEST_TIMEOUT);
        }

        if (!resetCredentials.getNewPassword().equals(resetCredentials.getNewPasswordConfirmed())){
            throw new ResetCredentialsException("Dine to kodeord stemmer ikke overens med hinanden!", HttpStatus.BAD_REQUEST);
        }

        String email = pwdResetTokenView.getEmail();
        //set user new password
        User userByUsername = userService.getUserByUsername(email);
        userByUsername.setPassword(passwordEncoder.encode(
                resetCredentials.getNewPassword())
        );
        return "OK";
    }
}
