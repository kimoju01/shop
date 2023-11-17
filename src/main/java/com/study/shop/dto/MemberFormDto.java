package com.study.shop.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data   // @ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberFormDto {
    // 회원 가입 화면에서 넘어오는 정보
    @NotBlank(message = "이름을 입력해 주세요.") // NULL 체크, 문자열인 경우 길이 0이거나 빈(" ") 문자열 검사
    private String name;
    
    @NotEmpty(message = "이메일을 입력해 주세요.")    // NULL 체크, 문자열인 경우 길이 0인지 검사
    @Email(message = "이메일 형식으로 입력해 주세요.")   // 이메일 형식인지 검사
    private String email;
    
    @NotEmpty(message = "비밀번호를 입력해 주세요.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해 주세요.")   // 최소, 최대 길이 검사
    private String password;
    
    @NotEmpty(message = "주소를 입력해 주세요.")
    private String address;

    public void setPassword(String password) {
        this.password = password;
    }

}
