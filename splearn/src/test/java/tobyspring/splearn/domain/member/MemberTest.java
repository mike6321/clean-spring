package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.member.MemberFixture.createPasswordEncoder;


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
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }
    
//    @Test
//    void constructorNullCheck() {
//        MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest(null);
//        assertThatThrownBy(() -> Member.register(memberRegisterRequest, null))
//                .isInstanceOf(NullPointerException.class);
//    }

    @Test
    void activate() {
        // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
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
        assertThat(member.getDetail().getDeActivatedAt()).isNotNull();
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

    @Test
    void updateInfo() {
        this.member.activate();
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("newNickname", "new1234", "newPhone");
        this.member.updateInfo(updateRequest);

        assertThat(this.member.getDetail().getProfile().address()).isEqualTo(updateRequest.profileAddress());
        assertThat(this.member.getNickname()).isEqualTo(updateRequest.nickname());
        assertThat(this.member.getDetail().getIntroduction()).isEqualTo(updateRequest.introduction());
    }


}
