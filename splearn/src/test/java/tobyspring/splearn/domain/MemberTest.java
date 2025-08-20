package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new PasswordEncoder() {
            @Override
            @NonNull
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(@NonNull String password, @NonNull String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest("junwoo.choi@test.com", "junwoo", "secret");
        this.member = Member.register(memberRegisterRequest, passwordEncoder);
    }

    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }
    
    @Test
    void constructorNullCheck() {
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest(null, "junwoo", "secret");
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
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("junwoo");
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
        assertThatThrownBy(() -> Member.register(new MemberRegisterRequest("INVALID_EMAIL", "junwoo", "secret"), passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
