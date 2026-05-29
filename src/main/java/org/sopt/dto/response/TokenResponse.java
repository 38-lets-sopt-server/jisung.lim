package org.sopt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// 토큰 발급 응답 (서버 → 클라이언트)
// 로그인 성공 시, 토큰 재발급 성공 시 둘 다 이 형식으로 반환
// 토큰 만료시각도 함꼐 반환
@Schema(description = "토큰 발급 응답")
public record TokenResponse(
        @Schema(description = "Access Token") String accessToken,

        @Schema(description = "Refresh Token") String refreshToken,

        @Schema(description = "Access Token 만료까지 남은 시간 (초)",
                example = "1800") long accessTokenExpiresIn,

        @Schema(description = "Refresh Token 만료까지 남은 시간 (초)",
                example = "1209600") long refreshTokenExpiresIn
) {
    public static TokenResponse of(
            String accessToken,
            String refreshToken,
            long accessTokenExpiresIn,
            long refreshTokenExpiresIn
    ) {
        return new TokenResponse(
                accessToken,
                refreshToken,
                accessTokenExpiresIn,
                refreshTokenExpiresIn
        );
    }
}
