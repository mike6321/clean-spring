package tobyspring.splearn.application.member.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;
import tobyspring.splearn.domain.member.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.member.MemberFixture.createPasswordEncoder;

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
        flushAndCler();

        // then
        assertThat(saved.getId()).isNotNull();
        Member found = memberRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }

    private void flushAndCler() {
        this.entityManager.clear();
        this.entityManager.flush();
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
