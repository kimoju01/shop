package com.study.shop.service;

import com.study.shop.dto.MemberFormDto;
import com.study.shop.entity.Member;
import com.study.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  // DB 작업 처리하다가 에러 하나라도 발생하면 로직 수행 전 상태로 모두 되돌려줌
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long register(MemberFormDto memberFormDto) {
        // 중복 회원 확인
        validateDuplicateMember(memberFormDto);

        // 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(memberFormDto.getPassword());
        memberFormDto.setPassword(encodePassword);

        // DTO -> Entity로 변환
        Member member = modelMapper.map(memberFormDto, Member.class);
        // USER 권한으로 설정
        member.setUserRole();
        Long id = memberRepository.save(member).getId();

        return id;
    }

    @Override
    public void validateDuplicateMember(MemberFormDto memberFormDto) {
        memberRepository.findByEmail(memberFormDto.getEmail())
                .ifPresent(member -> {  // 값이 존재하는 경우에 실행
                    throw new IllegalStateException("이미 가입된 회원입니다.");
                });

    }

}
