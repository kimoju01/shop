package com.study.shop.entity;

import com.study.shop.constant.Role;
import com.study.shop.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)    // Enum은 기본적으로 순서가 저장되어서 순서가 바뀔 경우 문제 발생 => 순서말고 String으로 저장하게 설정
    private Role role;

    public void setUserRole() {
        this.role = Role.USER;
    }

    public void setAdminRole() { this.role = Role.ADMIN; }

}
