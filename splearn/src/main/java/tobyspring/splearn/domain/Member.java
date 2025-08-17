package tobyspring.splearn.domain;

import lombok.Getter;

import static tobyspring.splearn.domain.MemberStatus.PENDING;

@Getter
public class Member {

    private final String email;
    private final String nickname;
    private final String passwordHash;
    private final MemberStatus status;

    public Member(String email, String nickname, String passwordHash) {
        this.email = email;
        this.nickname = nickname;
        this.passwordHash = passwordHash;

        this.status = PENDING;
    }

}
