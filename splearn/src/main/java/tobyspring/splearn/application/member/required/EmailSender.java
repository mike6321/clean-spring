package tobyspring.splearn.application.member.required;

import tobyspring.splearn.domain.shared.Email;

public interface  EmailSender {

    void send(Email email, String subject, String body);

}
