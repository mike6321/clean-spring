package tobyspring.splearn.application.member.required;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.member.provided.MemberFinder;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.*;
import tobyspring.splearn.domain.shared.Email;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class MemberModifyService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final MemberFinder memberFinder;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest request) {
        checkDuplicateEmail(request);

        Member member = Member.register(request, passwordEncoder);
        this.memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = this.memberFinder.find(memberId);

        member.activate();

        return this.memberRepository.save(member);
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = this.memberFinder.find(memberId);

        member.deactivate();

        return this.memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberInfoUpdateRequest updateRequest) {
        Member member = this.memberFinder.find(memberId);

        member.updateInfo(updateRequest);

        return this.memberRepository.save(member);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");
    }

    private void checkDuplicateEmail(MemberRegisterRequest request) {
        if (this.memberRepository.findByEmail(new Email(request.email())).isPresent()) {
            throw new DuplicationEmailException("이미 사용중인 이메일입니다. " + request.email());
        }
    }

}
