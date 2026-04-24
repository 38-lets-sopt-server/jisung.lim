package org.sopt.dto.response;

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
 *
 */
public record PageResponse<T>(List<T> content, int page, int size, boolean hasNext) {
}
