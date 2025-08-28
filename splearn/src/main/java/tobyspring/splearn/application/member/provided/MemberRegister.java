package tobyspring.splearn.application.member.provided;

import jakarta.validation.Valid;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

public interface MemberRegister {

    Member register(@Valid MemberRegisterRequest request);

    Member activate(Long memberId);

}
