package tobyspring.splearn.domain.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalIdCache;
import tobyspring.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.util.Assert.state;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
public class MemberDetail extends AbstractEntity {

    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deActivatedAt;

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();
        return memberDetail;
    }

    void activatedAt() {
        state(this.activatedAt == null, "이미 활성화된 회원입니다.");
        this.activatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        state(this.deActivatedAt == null, "이미 비활성화된 회원입니다.");
        this.deActivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.profile = new Profile(updateRequest.profileAddress());
        this.introduction = Objects.requireNonNull(updateRequest.introduction());
    }

}
