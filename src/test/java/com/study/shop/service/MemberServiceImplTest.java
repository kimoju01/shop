package com.study.shop.service;

import com.study.shop.dto.MemberFormDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Log4j2
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

//    public Member createMember() {
//        MemberFormDto memberFormDto = MemberFormDto.builder()
//                .email("test@naver.com")
//                .name("홍길동")
//                .address("부산광역시 해운대구")
//                .password("1234")
//                .build();
//
//        return Member.createMember(memberFormDto, passwordEncoder);
//    }

    public MemberFormDto createMemberDto() {
        return MemberFormDto.builder()
                .email("test@naver.com")
                .name("홍길동")
                .address("부산광역시 해운대구")
                .password("1234")
                .build();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void register() {
        MemberFormDto memberFormDto = createMemberDto();

        Long savedId = memberService.register(memberFormDto);
        Long expectedId = 1L;   // 새로 저장된 멤버의 id 기댓값
        log.info(savedId);

        assertEquals(expectedId, savedId);  // assertEquals(기댓값, 실제값) => 결과값 검증
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void registerDuplicate() {
        MemberFormDto memberFormDto1 = createMemberDto();
        MemberFormDto memberFormDto2 = createMemberDto();
        memberService.register(memberFormDto1);

        // 예외 처리 테스트. assertThrows(예상되는 예외 타입, 예외가 발생할 코드 블록 담은 람다 표현식)
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.register(memberFormDto2);
        });

        // 발생한 예외 메시지가 예상 결과와 맞는지 검증
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}