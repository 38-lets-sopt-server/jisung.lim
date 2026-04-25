package org.sopt.dto.response;

import org.sopt.domain.BoardType;
import org.sopt.domain.Post;

// 게시글 조회 응답 (서버 → 클라이언트)
public record PostResponse(
        Long id, String title, String content, String author, String createdAt, BoardType boardType
) {
    // 정적 팩토리 메서드: Post 도메인 객체를 PostResponse로 변환
    // record의 기본 생성자는 선언된 필드를 그대로 받기만 하므로,
    // Post 객체를 하나 받아서 내부 값을 추출하는 로직은 별도의 정적 메서드로 만든다
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getBoardType()
        );
    }
}
