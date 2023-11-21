package com.study.shop.config;

import com.study.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    // http 요청에 대한 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")    // 로그인 페이지 URL
                .defaultSuccessUrl("/")         // 로그인 성공 시 이동할 URL
                .usernameParameter("email")     // 로그인 시 사용할 파라미터 이름
                .failureUrl("/members/login/error") // 로그인 실패 시 이동할 URL
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))     // 로그아웃 URL
                .logoutSuccessUrl("/");         // 로그아웃 성공 시 이동할 URL

        http.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()    // 해당 경로 정적 리소스에 모든 사용자에게 허용
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()  // 해당 경로에 대해 모든 사용자에게 허용
                .mvcMatchers("/admin/**").hasRole("ADMIN")  // ADMIN에게만 허용
                .anyRequest().authenticated();  // 나머지 요청들은 인증된 사용자에게만 허용

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());    // 인증에 실패한 경우 처리할 AuthenticationEntryPoint

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
