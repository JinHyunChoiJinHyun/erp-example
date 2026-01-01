package com.example.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    // 보안 규칙 정의
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // cors 설정
                .cors(cors -> {})
                // csrf 비활성화 (fetch / axios) 구조에서 비활성화
                .csrf(csrf -> csrf.disable())
                // url 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 누구나 접근 가능 
                        .requestMatchers(
                                "/member/login",
                                "/member/signup",
                                "/task/**"
                        ).permitAll()
                        // 그 외의 접근은 모두 로그인 필요
                        .anyRequest().authenticated()
                )
                // email 필드를 아이디로 사용
                .formLogin(form -> form
                        .loginProcessingUrl("/member/login")
                        .usernameParameter("email")
                        // 로그인 실패 시
                        .failureHandler((request, response, exception) -> {
                            request.getSession().invalidate();
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                )
                // 로그아웃 설정
                .logout((logout) -> logout
                        // 로그아웃 성공 시
                        .logoutUrl("/member/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        // 서버 세션 삭제 및 브라우저 쿠키 제거
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }
    
    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 인증 처리 매니저 bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
