package com.study.shop.controller;

import com.study.shop.dto.MemberFormDto;
import com.study.shop.entity.Member;
import com.study.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
    public String memberRegisterPost(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        // @Vailid, BindingResult를 통해 DTO 검증
        log.info("Member Register POST..........");

        // 검증 시 에러가 있다면 회원 가입 페이지로 이동
        if (bindingResult.hasErrors()) {
            log.info("has error..........");
            return "member/memberRegister";         // 이렇게 하면 폼에 적은 내용 유지 (패스워드만 지워짐)
//            return "redirect:/members/register";  // 이렇게 하면 폼에 있는 내용 싹 다 지운다
        }

        log.info(memberFormDto);
        // 회원 가입 시 중복 회원 가입 예외 발생 시 에러 메시지를 뷰로 전달
        try {
            Long savedId = memberService.register(memberFormDto);
            log.info(savedId);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            log.info("errorMessage = " + e.getMessage());

            return "member/memberRegister";
        }

        return "redirect:/";
    }

}
