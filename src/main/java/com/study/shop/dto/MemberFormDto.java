package com.study.shop.dto;

import lombok.*;

@Data   // @ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberFormDto {
    // 회원 가입 화면에서 넘어오는 정보

    private String name;
    private String email;
    private String password;
    private String address;

}
