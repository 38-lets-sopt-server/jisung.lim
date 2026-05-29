package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.BoardType;

// 게시글 작성 요청 (클라이언트 → 서버)
// 작성자(userId): JWT 토큰의 principal에서 추출
@Schema(description = "게시글 작성 요청")
public record CreatePostRequest(
        @Schema(description = "게시글 제목 (최대 50자)",
                example = "오늘 학식 뭐임",
                requiredMode = Schema.RequiredMode.REQUIRED) String title,

        @Schema(description = "게시글 내용 (최대 2000자)",
                example = "돈까스래",
                requiredMode = Schema.RequiredMode.REQUIRED) String content,

        @Schema(description = "게시판 종류 (FREE / HOT / SECRET)",
                example = "FREE",
                requiredMode = Schema.RequiredMode.REQUIRED) BoardType boardType
) {
}
