package tobyspring.splearn.domain;

import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;
import static tobyspring.splearn.domain.MemberStatus.*;

@Getter
@ToString
public class Member {

    private final String email;
    private String nickname;
    private String passwordHash;
    private MemberStatus status;

    private Member(String email, String nickname, String passwordHash) {
        this.email = requireNonNull(email);
        this.nickname = requireNonNull(nickname);
        this.passwordHash = requireNonNull(passwordHash);

        this.status = PENDING;
    }

    public void activate() {
        state(this.status == PENDING, "PENDING 상태의 회원만 활성화할 수 있습니다.");

        this.status = ACTIVE;
    }

    public void deactivate() {
        state(this.status == ACTIVE, "활성화된 회원만 비활성화할 수 있습니다.");

        this.status = DEACTIVATED;
    }

    public static Member create(String email, String nickname, String password, PasswordEncoder passwordEncoder) {
        return new Member(email, nickname, passwordEncoder.encode(password));
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(password);
    }
}
