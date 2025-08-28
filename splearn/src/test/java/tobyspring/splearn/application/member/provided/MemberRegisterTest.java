package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.member.MemberStatus.ACTIVE;
import static tobyspring.splearn.domain.member.MemberStatus.DEACTIVATED;

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
        Member member = createMember();

        member = memberRegister.activate(member.getId());

        flushAndClear();

        assertThat(member.getStatus()).isEqualTo(ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate() {
        Member member = createMember();

        member = memberRegister.activate(member.getId());
        flushAndClear();

        member = memberRegister.deactivate(member.getId());
        flushAndClear();

        assertThat(member.getStatus()).isEqualTo(DEACTIVATED);
        assertThat(member.getDetail().getDeActivatedAt()).isNotNull();
    }

    @Test
    void updateInfo () {
        Member member = createMember();

        member = memberRegister.activate(member.getId());
        flushAndClear();

        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("newNickname", "new1234", "newPhone");
        member = memberRegister.updateInfo(member.getId(), updateRequest);
        flushAndClear();

        assertThat(member.getDetail().getProfile().address()).isEqualTo(updateRequest.profileAddress());
    }

    private Member createMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
        flushAndClear();
        return member;
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

}
