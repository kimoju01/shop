package com.study.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    // JPA Auditing으로 엔티티가 저장 또는 수정될 때 자동으로 현재 로그인한 사용자의 정보를 생성자와 수정자로 지정

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = "";
        if (authentication != null) {
            userId = authentication.getName();  // 현재 로그인 한 사용자의 정보 조회해 사용자의 이름을 등록자와 수정자로 지정
        }
        return Optional.of(userId);
    }
}
