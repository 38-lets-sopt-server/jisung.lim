package org.sopt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 페이지네이션용 응답 wrapper
 * 제네릭 T: 페이지 항목 타입
 * ex: 게시글 페이지네이션이면 PostResponse, 댓글 페이지네이션이면 CommentResposne
 * <p>
 * content: 현재 페이지의 실제 데이터 배열
 * page: 현재 페이지 번호 (0부터 시작)
 * size: 페이지당 목록 개수
 * hasNext: 다음 페이지 존재 여부 (무한 스크롤 종료 판단용)
 */
@Schema(description = "페이지네이션 응답")
public record PageResponse<T>(
        @Schema(description = "현재 페이지의 데이터 목록")
        List<T> content,

        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지 크기", example = "10")
        int size,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
}
