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
    private final String nickname;
    private final String passwordHash;
    private MemberStatus status;

    public Member(String email, String nickname, String passwordHash) {
        this.email = requireNonNull(email);
        this.nickname = requireNonNull(nickname);
        this.passwordHash = passwordHash;

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

}
