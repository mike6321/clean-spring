package tobyspring.splearn.application.required;

import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

    Optional<Member> findByEmail(Email email);

}
