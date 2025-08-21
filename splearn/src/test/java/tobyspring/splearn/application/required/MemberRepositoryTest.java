package tobyspring.splearn.application.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
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
    
    @Test
    void duplicatedEmailFail() {
        // given
        MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest("test@test.com");
        PasswordEncoder passwordEncoder = createPasswordEncoder();

        Member member1 = Member.register(memberRegisterRequest, passwordEncoder);
        this.memberRepository.save(member1);

        // when & then
        Member member2 = Member.register(memberRegisterRequest, passwordEncoder);
        assertThatThrownBy(() -> this.memberRepository.save(member2)).isInstanceOf(DataIntegrityViolationException.class);

    }
    


}
