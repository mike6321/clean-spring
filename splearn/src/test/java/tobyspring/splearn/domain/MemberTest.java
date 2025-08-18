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
        this.member = Member.create("junwoo.choi@test.com", "junwoo", "secret", passwordEncoder);
    }

    @Test
    void createMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }
    
    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> Member.create(null, "junwoo", "secret", null))
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

}
