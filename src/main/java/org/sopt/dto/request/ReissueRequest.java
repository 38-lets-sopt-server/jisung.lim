package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 토큰 재발급 요청 (클라이언트 → 서버)
// Access Token이 만료됐을 때 클라가 갖고 있던 Refresh Token을 실어서 호출
// AuthService.reissue()가 이 값을 DB에서 조회하고 검증
@Schema(description = "토큰 재발급 요청")
public record ReissueRequest(
        @Schema(description = "Refresh Token",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.xxxxx",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String refreshToken
) {
}
