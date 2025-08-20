package tobyspring.splearn.application.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static tobyspring.splearn.fixture.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.fixture.MemberFixture.createPasswordEncoder;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void createMember() {
        // given
        MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest("test@test.com");
        PasswordEncoder passwordEncoder = createPasswordEncoder();

        // when
        Member member = Member.register(memberRegisterRequest, passwordEncoder);
        Member saved = this.memberRepository.save(member);
        this.entityManager.flush();

        // then
        assertThat(saved.getId()).isNotNull();
    }


}
