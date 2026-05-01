package org.sopt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.BoardType;
import org.sopt.domain.Post;

import java.time.LocalDateTime;

// 게시글 조회 응답 (서버 → 클라이언트)
@Schema(description = "게시글 조회 응답")
public record PostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,

        @Schema(description = "게시글 제목", example = "오늘 학식 뭐임")
        String title,

        @Schema(description = "게시글 내용", example = "돈까스래")
        String content,

        @Schema(description = "작성자 닉네임", example = "테스트유저")
        String nickname,

        @Schema(description = "작성 시각", example = "2026-05-01T17:30:00")
        LocalDateTime createdAt,

        @Schema(description = "게시판 종류", example = "FREE")
        BoardType boardType
) {
    // 2주차
    // 정적 팩토리 메서드: Post 도메인 객체를 PostResponse로 변환
    // record의 기본 생성자는 선언된 필드를 그대로 받기만 하므로,
    // Post 객체를 하나 받아서 내부 값을 추출하는 로직은 별도의 정적 메서드로 만든다
    //
    // 3주차
    // Post.user는 LAZY 로딩 -> 이 메서드가 호출되는 시점에 user 프록시가 초기화됨
    // 게시글 목록 전체 조회처럼 여러 게시글을 조회하는 경우 getUser()로 user DB에 접근할 때마다
    // user 프록시가 매번 초기화되어 N+1 문제 발생 가능 -> fetch join으로 해결 필요
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt(),
                post.getBoardType()
        );
    }
}
