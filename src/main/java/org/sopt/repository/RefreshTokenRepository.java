package org.sopt.repository;

import org.sopt.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 토큰 재발급에 사용 — 클라가 보낸 refresh token으로 DB row 조회
    // 없으면 Optional.empty() 반환 -> 서비스에서 예외로 변환
    Optional<RefreshToken> findByToken(String token);

    // 로그인에 사용 — 같은 유저가 이미 발급받은 refresh가 있는지 확인
    // 있으면 rotate, 없으면 새로 save
    Optional<RefreshToken> findByUserId(Long userId);

    // 로그아웃에 사용 — 해당 유저의 refresh row 삭제
    void deleteByUserId(Long userId);
}