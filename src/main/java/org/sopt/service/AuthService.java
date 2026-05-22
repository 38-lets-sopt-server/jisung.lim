package org.sopt.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.sopt.common.ErrorCode;
import org.sopt.domain.RefreshToken;
import org.sopt.domain.User;
import org.sopt.dto.request.LoginRequest;
import org.sopt.dto.request.ReissueRequest;
import org.sopt.dto.response.TokenResponse;
import org.sopt.exception.BusinessException;
import org.sopt.repository.RefreshTokenRepository;
import org.sopt.repository.UserRepository;
import org.sopt.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// 인증 관련 비즈니스 로직
// JwtService는 JWT 라이브러리 wrapper(기술 계층), AuthService는 도메인 로직 조립(비즈니스 계층)
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    // 로그인
    // 1. 이메일로 유저 조회 — 없으면 INVALID_CREDENTIALS
    // 2. 비번 비교 — 틀리면 INVALID_CREDENTIALS
    // 3. Access/Refresh 토큰 발급
    // 4. Refresh 토큰을 DB에 저장 — 기존에 있으면 rotate, 없으면 새로운 토큰 save
    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!user.getPassword().equals(request.password())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        // 기존 refresh 있으면 rotate, 없으면 새 토큰 save
        Optional<RefreshToken> existing = refreshTokenRepository.findByUserId(user.getId());
        existing.ifPresentOrElse(
                rt -> rt.rotate(refreshToken, jwtService.getRefreshTokenExpiresInSeconds()),
                () -> refreshTokenRepository.save(new RefreshToken(
                        user.getId(),
                        refreshToken,
                        jwtService.getRefreshTokenExpiresInSeconds()
                ))
        );

        // @Transactional -> rotate는 더티 체킹으로 자동 UPDATE — 별도 save() 호출 불필요

        return TokenResponse.of(
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpiresInSeconds(),
                jwtService.getRefreshTokenExpiresInSeconds()
        );
    }

    // 토큰 재발급
    // 1. DB에서 refresh 조회 — 없으면 REFRESH_TOKEN_NOT_FOUND
    // 2. 만료 확인 — 만료면 REFRESH_TOKEN_EXPIRED
    // 3. JWT signature 검증 — 서명 깨졌으면 INVALID_TOKEN
    // 4. 새 Access + 새 Refresh 발급
    // 5. DB의 기존 row를 새 refresh로 rotate (옛 refresh는 즉시 무효화)
    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        RefreshToken stored = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (stored.isExpired()) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        Long userId;
        try {
            userId = jwtService.verifyAndGetUserId(request.refreshToken());
        } catch (JWTVerificationException e) {
            // 서명 깨짐 / 만료 등 — 일단 INVALID_TOKEN으로 묶음
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 새 토큰 발급
        // AccessToken 발급 시 email도 필요하므로 user 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        // 기존 row를 새 refresh로 rotate (덮어쓰기) —> 옛 refresh는 더 이상 DB에 없으므로 재사용 불가
        stored.rotate(newRefreshToken, jwtService.getRefreshTokenExpiresInSeconds());

        return TokenResponse.of(
                newAccessToken,
                newRefreshToken,
                jwtService.getAccessTokenExpiresInSeconds(),
                jwtService.getRefreshTokenExpiresInSeconds()
        );
    }
}
