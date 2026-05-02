package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 좋아요 요청 (클라이언트 → 서버)
// POST /api/v1/posts/{postId}/likes 와 DELETE /api/v1/posts/{postId}/likes 양쪽에서 재사용
@Schema(description = "좋아요 요청")
public record CreateLikeRequest(
        @Schema(description = "좋아요를 누르는 사용자 ID",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) Long userId
) {
}
