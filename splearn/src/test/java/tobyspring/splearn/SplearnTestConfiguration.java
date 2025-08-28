package tobyspring.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.domain.member.PasswordEncoder;
import tobyspring.splearn.domain.member.MemberFixture;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    public EmailSender emailSender() {
        return (email, subject, body) -> System.out.println("send email: " + email + ", subject: " + subject + ", body: " + body);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
