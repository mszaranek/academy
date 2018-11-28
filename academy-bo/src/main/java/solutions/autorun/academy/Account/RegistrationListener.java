package solutions.autorun.academy.Account;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.services.UserService;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private UserService userService;
    private MessageSource messageSource;
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
    this.confirmRegistration(onRegistrationCompleteEvent);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String reciptientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
      //  String message = messageSource.getMessage("message.regSucc",null,event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(reciptientAddress);
        email.setSubject(subject);
        //message + " rn" +
        email.setText("http://54.37.73.221:8888" + confirmationUrl);
        mailSender.send(email);
    }
}
