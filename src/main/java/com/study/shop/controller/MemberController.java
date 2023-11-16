package com.study.shop.controller;

import com.study.shop.dto.MemberFormDto;
import com.study.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String memberRegisterGet(Model model) {
        log.info("Member Register GET..........");

        model.addAttribute("memberFormDto", new MemberFormDto());
        // memberFormDto 객체를 할당한 이유?
        // 뷰에서 폼 데이터 바인딩할 때 해당 폼 데이터가 null이 아니라 초기화된 객체로 시작하면 처리하기 좋음 (유효성 검증)
        return "member/memberRegister";
    }

    @PostMapping("/register")
    public String memberRegisterPost(MemberFormDto memberFormDto) {
        log.info("Member Register POST..........");

        log.info(memberFormDto);
        Long savedId = memberService.register(memberFormDto);
        log.info(savedId);

        return "redirect:/";
    }

}
