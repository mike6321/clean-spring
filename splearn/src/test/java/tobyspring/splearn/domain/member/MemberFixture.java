package tobyspring.splearn.domain.member;

import org.springframework.lang.NonNull;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static MemberRegisterRequest createMemberRegisterRequest(String mail) {
        return new MemberRegisterRequest(mail, "junwoo-great", "secret12345");
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return new MemberRegisterRequest("mail@test.com", "junwoo-great", "secret12345");
    }

    public static Member createMember(Long id) {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
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
    }

}
