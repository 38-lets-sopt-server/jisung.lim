package org.sopt.dto.request;

import org.sopt.domain.BoardType;

// 게시글 작성 요청 (클라이언트 → 서버)
// record: 데이터만 담는 불변 객체를 한 줄로 선언 가능!
// title, content, userId, boardType을 final 필드로 선언(불변)
// 네 값을 받는 생성자 자동 생성
// 각 필드에 대응하는 접근자 메서드 title(), content(), userId(), boardType() 자동 생성
public record CreatePostRequest(
        String title, String content, Long userId, BoardType boardType
) {
}
