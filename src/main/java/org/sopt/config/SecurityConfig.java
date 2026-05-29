package org.sopt.config;

import org.sopt.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // SecurityFilterChain — Spring Security의 모든 인증/인가 규칙을 모은 빈
    // Spring Boot가 자동 생성하는 기본 체인(모든 요청 인증 강제 + HTTP Basic) 대신 사용자 정의 체인을 사용
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // JWT는 stateless라 CSRF 토큰 검증 불필요 — 끔
                .csrf(AbstractHttpConfigurer::disable)
                // Spring Boot의 자동 HTTP Basic / Form Login 끔 (우리는 JWT 쓰니까)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                // 세션 안 씀 — 매 요청마다 토큰으로 인증 (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인가 규칙 — URL별로 인증 필요 여부 명시
                // 인증 관련 엔드포인트는 비로그인 허용
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // 게시글 조회는 비로그인 허용
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**")
                        .permitAll()
                        // 나머지 전부 인증 필요
                        .anyRequest()
                        .authenticated())

                // 인증 실패 시(SecurityContext에 인증 객체 없을 때) 401 반환
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(org.springframework.http.HttpStatus.UNAUTHORIZED)))

                // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 끼워 넣음
                // -> 요청이 들어올 때마다 JwtAuthFilter가 먼저 돌면서 SecurityContext에 인증 set
                // -> 그 뒤 인가 검사 단계에서 인증 객체 유무로 401/200 결정
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 비밀번호 인코더 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
