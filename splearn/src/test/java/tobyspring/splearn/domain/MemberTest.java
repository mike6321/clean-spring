package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.fixture.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.fixture.MemberFixture.createPasswordEncoder;


class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = createPasswordEncoder();
        MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest("junwoo.choi@test.com");
        this.member = Member.register(memberRegisterRequest, passwordEncoder);
    }

    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }
    
    @Test
    void constructorNullCheck() {
        MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest(null);
        assertThatThrownBy(() -> Member.register(memberRegisterRequest, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        // when
        member.activate();

        // then
        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deActivate() {
        // when
        member.activate();
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deActivateFail() {
        // when & then
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("secret12345", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("junwoo-great");
        member.changeNickname("junwoo.choi");
        assertThat(member.getNickname()).isEqualTo("junwoo.choi");
    }

    @Test
    void changePassword() {
        member.changePassword("newSecret", passwordEncoder);
        assertThat(member.verifyPassword("newSecret", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        assertThat(member.isActive()).isFalse();
        member.activate();
        assertThat(member.isActive()).isTrue();
        member.deactivate();
        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail() {
        assertThatThrownBy(() -> Member.register(createMemberRegisterRequest("INVALID_EMAIL"), passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
