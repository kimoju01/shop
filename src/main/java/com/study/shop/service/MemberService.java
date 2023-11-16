package com.study.shop.service;

import com.study.shop.dto.MemberFormDto;

public interface MemberService {

    Long register(MemberFormDto memberFormDto);
    void validateDuplicateMember(MemberFormDto memberFormDto);

}
