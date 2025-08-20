package tobyspring.splearn.application.required;

import tobyspring.splearn.domain.Email;

public interface EmailSender {

    void send(Email email, String subject, String body);

}
