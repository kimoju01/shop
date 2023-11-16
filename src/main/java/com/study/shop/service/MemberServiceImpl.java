package com.study.shop.service;

import com.study.shop.dto.MemberFormDto;
import com.study.shop.entity.Member;
import com.study.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  // DB 작업 처리하다가 에러 하나라도 발생하면 로직 수행 전 상태로 모두 되돌려줌
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(MemberFormDto memberFormDto) {
        validateDuplicateMember(memberFormDto);

        Member member = modelMapper.map(memberFormDto, Member.class);
        Long id = memberRepository.save(member).getId();

        return id;
    }

    @Override
    public void validateDuplicateMember(MemberFormDto memberFormDto) {
        Member findMember = memberRepository.findByEmail(memberFormDto.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

}
