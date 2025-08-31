package tobyspring.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiTest {

    @MockitoBean
    private MemberRegister memberRegister;
    final MockMvcTester mockMvcTester;
    final ObjectMapper objectMapper;

    @Test
    void register() throws JsonProcessingException {
        Member member = MemberFixture.createMember(1L);
        when(memberRegister.register(any())).thenReturn(member);

        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(memberRegisterRequest);

        assertThat(
                mockMvcTester.post().uri("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId").isEqualTo(1);

        verify(memberRegister).register(memberRegisterRequest);
    }

    @Test
    void registerFail() throws JsonProcessingException {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest("invalidEmail");
        String requestJson = objectMapper.writeValueAsString(memberRegisterRequest);

        assertThat(
                mockMvcTester.post().uri("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

}
