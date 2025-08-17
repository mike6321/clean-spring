package tobyspring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest {

    @Test
    void createMember() {
        Member member = new Member("junwoo.choi@test.com", "junwoo", "secret");
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }
    
    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> new Member(null, "junwoo", "1234"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        // given
        var member = new Member("junwoo.choi@test.com", "junwoo", "secret");

        // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        // given
        var member = new Member("junwoo.choi@test.com", "junwoo", "secret");

        // when
        member.activate();

        // then
        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deActivate() {
        // given
        var member = new Member("junwoo.choi@test.com", "junwoo", "secret");

        // when
        member.activate();
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deActivateFail() {
        // given
        var member = new Member("junwoo.choi@test.com", "junwoo", "secret");

        // when & then
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }


}
