package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.BoardType;

// 게시글 작성 요청 (클라이언트 → 서버)
// record: 데이터만 담는 불변 객체를 한 줄로 선언 가능!
// title, content, userId, boardType을 final 필드로 선언(불변)
// 네 값을 받는 생성자 자동 생성
// 각 필드에 대응하는 접근자 메서드 title(), content(), userId(), boardType() 자동 생성
@Schema(description = "게시글 작성 요청")
public record CreatePostRequest(
        @Schema(description = "게시글 제목 (최대 50자)", example = "오늘 학식 뭐임",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "게시글 내용 (최대 2000자)", example = "돈까스래",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String content,

        @Schema(description = "작성자 사용자 ID (사전에 등록된 유저)", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Long userId,

        @Schema(description = "게시판 종류 (FREE / HOT / SECRET)", example = "FREE",
                requiredMode = Schema.RequiredMode.REQUIRED)
        BoardType boardType
) {
}
