package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.DuplicationEmailException;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;
import tobyspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ContextConfiguration(classes = SplearnTestConfiguration.class)
@Transactional
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));

        System.out.println("member = " + member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com")))
                .isInstanceOf(DuplicationEmailException.class);
    }

    @Test
    void memberRegisterRequestFail() {
        MemberRegisterRequest invalid1 = new MemberRegisterRequest("test@test.com", "test", "test");
        extracted(invalid1);

        MemberRegisterRequest invalid2 = new MemberRegisterRequest("test@test.com", "test12342134213432432432412341234", "test");
        extracted(invalid2);
    }

    private void extracted(MemberRegisterRequest invalid1) {
        assertThatThrownBy(() -> memberRegister.register(invalid1))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }


}
