package tobyspring.splearn.application.provided;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splearn.application.MemberService;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberStatus;
import tobyspring.splearn.fixture.MemberFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MemberRegisterTest {

    @Test
    void memberRegister() {
        MemberRegister memberRegister = new MemberService(new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder());
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void memberRegisterMock() {
        EmailSenderMock emailSenderMock = new EmailSenderMock();
        MemberRegister memberRegister = new MemberService(new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder());
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSenderMock.getTos()).size().isEqualTo(1);
        assertThat(emailSenderMock.getTos().getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    void memberRegisterMockito() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
        MemberRegister memberRegister = new MemberService(new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder());
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }
    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {

        }
    }

    static class EmailSenderMock implements EmailSender {
        List<Email> tos = new ArrayList<>();
        @Override
        public void send(Email email, String subject, String body) {
            tos.add(email);
        }

        public List<Email> getTos() {
            return tos;
        }
    }
}
