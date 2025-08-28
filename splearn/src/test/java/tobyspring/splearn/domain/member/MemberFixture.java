package tobyspring.splearn.domain.member;

import org.springframework.lang.NonNull;

public class MemberFixture {

    public static MemberRegisterRequest createMemberRegisterRequest(String mail) {
        return new MemberRegisterRequest(mail, "junwoo-great", "secret12345");
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
