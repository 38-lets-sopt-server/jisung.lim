package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 로그인 요청 (클라이언트 → 서버)
// 이메일 + 비밀번호를 받아 AuthService.login()에서 검증
@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "이메일", example = "test@sopt.org",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "비밀번호", example = "password123",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
