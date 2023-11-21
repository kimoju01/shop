package com.study.shop.controller;

import com.study.shop.dto.MemberFormDto;
import com.study.shop.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc   // MockMvc 테스트 위한 어노테이션
@Log4j2
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;    // MockMvc 객체 이용하면 웹 브라우저에서 요청 하는 것 처럼 테스트 가능하다.

    @Autowired
    PasswordEncoder passwordEncoder;

    public MemberFormDto createMemberDto(String email, String password) {
        return MemberFormDto.builder()
                .email(email)
                .name("홍길동")
                .address("부산광역시 해운대구")
                .password(password)
                .build();
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@naver.com";
        String password = "1234";

        MemberFormDto memberFormDto = createMemberDto(email, password);
        memberService.register(memberFormDto);

        mockMvc.perform(formLogin().userParameter("email")  // HTTP POST 요청 생성해서 로그인 테스트
                .loginProcessingUrl("/members/login")
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());  // 로그인 성공 시 테스트 통과
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test@naver.com";
        String password = "1234";

        MemberFormDto memberFormDto = createMemberDto(email, password);
        memberService.register(memberFormDto);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());    // 로그인 실패 시 테스트 통과
    }

}
