package org.sopt.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// 모든 요청 진입 시 한 번씩 동작하는 인증 필터 (interceptor 패턴)
// 1. Authorization 헤더에서 Bearer 토큰 추출
// 2. JwtService로 토큰 검증 + userId 추출
// 3. 성공 시 SecurityContext에 인증 객체 set → 컨트롤러에서 @AuthenticationPrincipal로 꺼내 쓸 수 있음
// 4. 실패 시 SecurityContext 비워둔 채로 다음 필터로 넘김
// SecurityContext에 인증 객체가 들어있으면 인증된 것, 비어있으면 인증되지 않은 것
// 토큰이 포함되어 소면 그 토큰에 담긴 정보를 SecurityContext에 넣음
// 토큰이 없거나/위조되었거나/만료되었으면 SecurityContext를 비우고 다음 작업 넘어감 -> 뒤에서 에러처리
// OncePerRequestFilter 상속 — 한 요청에서 필터가 한 번만 실행되는 걸 보장
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractBearerToken(request);

        // 토큰이 있으면 검증 시도. 없으면 그냥 다음 필터로 (인증 안 된 상태 유지)
        if (token != null) {
            try {
                Long userId = jwtService.verifyAndGetUserId(token);

                // Spring Security가 이해하는 형식의 인증 객체 생성
                // - 1번째 인자: principal — 컨트롤러에서 @AuthenticationPrincipal로 꺼내 쓸 값. 우리는 userId
                // - 2번째 인자: credentials — 보통 password. 토큰 인증에선 의미 없으니 null
                // - 3번째 인자: authorities — 권한 목록
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,
                        null,
                        List.of()
                );

                // SecurityContext에 set — 이후 같은 요청 처리 동안 SecurityContextHolder.getContext()로 꺼낼 수 있음
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTVerificationException | IllegalArgumentException e) {
                // 검증 실패 시 SecurityContext 그대로 두면 됨 (인증 안 됨 상태)
                // 별도 응답 안 보냄 — SecurityConfig의 EntryPoint가 401 처리 담당
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 "Bearer " 떼고 토큰 문자열만 반환
    // 헤더 없거나 Bearer 형식 아니면 null
    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return header.substring(BEARER_PREFIX.length());
    }
}
