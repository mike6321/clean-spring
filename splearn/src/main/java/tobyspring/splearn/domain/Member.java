package tobyspring.splearn.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;
import static tobyspring.splearn.domain.MemberStatus.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
public class Member extends AbstractEntity {

    @NaturalId
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    public void activate() {
        state(this.status == PENDING, "PENDING 상태의 회원만 활성화할 수 있습니다.");

        this.status = ACTIVE;
    }

    public void deactivate() {
        state(this.status == ACTIVE, "활성화된 회원만 비활성화할 수 있습니다.");

        this.status = DEACTIVATED;
    }

    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        String email = createRequest.email();

        member.email = new Email(requireNonNull(email));
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(createRequest.password()));

        member.status = PENDING;

        return member;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == ACTIVE;
    }

}
