package org.sopt.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User 객체 자체가 필요한 시나리오가 없으므로 @ManyToOne 적용할 필요 X
    @Column(nullable = false, unique = true)
    private Long userId;

    // refresh token 문자열 자체
    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    // 토큰 만료 시각. JWT 자체에도 exp가 있지만, DB에 별도로 두면 SQL로 만료된 row를 일괄 정리하기 쉬움
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // JPA 기본 생성자 (외부에서 빈 객체 생성 막기)
    protected RefreshToken() {
    }

    public RefreshToken(Long userId, String token, long expiresInSeconds) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = LocalDateTime.now().plusSeconds(expiresInSeconds);
    }

    // Rotate: 재발급 시 기존 row를 그대로 두고 token/만료시각만 바꿔치기
    //   더티 체킹으로 UPDATE 쿼리 자동 요청
    public void rotate(String newToken, long expiresInSeconds) {
        this.token = newToken;
        this.expiresAt = LocalDateTime.now().plusSeconds(expiresInSeconds);
    }

    // 만료 여부를 엔티티 내에서 판단 — 호출부에서 매번 LocalDateTime.now()와 비교하는 중복 제거
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
