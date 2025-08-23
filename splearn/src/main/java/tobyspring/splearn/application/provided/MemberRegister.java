package tobyspring.splearn.application.provided;

import jakarta.validation.Valid;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;

public interface MemberRegister {

    Member register(@Valid MemberRegisterRequest request);

    Member activate(Long memberId);

}
