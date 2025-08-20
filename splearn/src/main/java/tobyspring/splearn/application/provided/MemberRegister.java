package tobyspring.splearn.application.provided;

import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;

public interface MemberRegister {

    Member register(MemberRegisterRequest request);

}
