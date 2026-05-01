package org.sopt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 ID 응답 (작성/수정/삭제 결과)")
public record PostIdResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id
) {
}
