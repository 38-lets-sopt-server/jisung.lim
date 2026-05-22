package org.sopt.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    // 서명/검증에 사용하는 알고리즘 객체
    // 생성자에서 한 번 만들어두고 재사용
    private final Algorithm algorithm;

    // 토큰 만료 시간 (초단위), application.yml의 security.jwt.* 값에서 주입
    private final long accessTokenExpiresInSeconds;
    private final long refreshTokenExpiresInSeconds;

    // 생성자 주입
    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-expires-in-seconds:1800}") long accessTokenExpiresInSeconds,
            @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}") long refreshTokenExpiresInSeconds
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.accessTokenExpiresInSeconds = accessTokenExpiresInSeconds;
        this.refreshTokenExpiresInSeconds = refreshTokenExpiresInSeconds;
    }

    // AccessToken 발급
    // JWT = 헤더.페이로드.서명
    public String generateAccessToken(Long userId, String email) {
        Instant now = Instant.now();
        return JWT.create()
                // JWT 페이로드에 userId 정보
                .withSubject(String.valueOf(userId))
                // JWT 페이로드에 email 정보
                .withClaim("email", email)
                // AccessToken 만료시간 설정
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(accessTokenExpiresInSeconds)))
                .sign(algorithm);
    }

    // Refresh Token 발급
    // email 같은 부가 정보는 안 넣음 — 재발급에만 쓰일 거라 userId(sub)만 있으면 충분
    // 만료시각만 길게 (기본 2주)
    public String generateRefreshToken(Long userId) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(refreshTokenExpiresInSeconds)))
                .sign(algorithm);
    }

    // 토큰 검증 + sub(userId) 추출
    // 검증 실패 시(서명 불일치/만료 등) auth0 라이브러리가 JWTVerificationException 계열 에러 throw
    // -> 그건 호출부(JwtAuthFilter, AuthService.reissue)에서 처리
    public Long verifyAndGetUserId(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        // 서명 불일치 -> SignatureVerificationException
        // 만료 -> TokenExpiredException
        // 둘 다 JWTVerificationException 의 자식
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        try {
            return Long.parseLong(jwt.getSubject());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("JWT의 사용자 정보가 올바르지 않습니다.");
        }
    }
}
