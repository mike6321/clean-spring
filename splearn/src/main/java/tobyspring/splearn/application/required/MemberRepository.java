package tobyspring.splearn.application.required;

import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.Member;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

}
