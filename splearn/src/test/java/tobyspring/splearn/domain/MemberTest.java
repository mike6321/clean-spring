package tobyspring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MemberTest {

    @Test
    void createMember() {
        Member member = new Member("junwoo.choi@test.com", "junwoo", "secret");
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

}
