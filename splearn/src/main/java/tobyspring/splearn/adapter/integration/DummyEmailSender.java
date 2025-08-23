package tobyspring.splearn.adapter.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.Email;

@Slf4j
@Component
@Fallback
public class DummyEmailSender implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("send email: " + email + ", subject: " + subject + ", body: " + body);
    }

}
