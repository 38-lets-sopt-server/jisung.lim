package org.sopt.repository;

import org.sopt.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // JpaRepository의 기본 메서드로는 특정 user_id + post_id의 조합으로
    // like가 존재하는지 찾을 수 없음 =
    // -> 커스텀 메서드 선언 필요 (특정 user_id + post_id로 like 조회, like 반환)

    // Spring Data JPA 메서드 이름 규칙
    // 메서드 이름을 existsByUserIdAndPostId 처럼 적기만 하면, Spring Data JPA가 그 이름을 분석해서
    // SQL을 자동으로 생성해준다!
    // Spring Data JPA는 중첩 속성 접근도 인식한다.
    // Like 엔티티의 User user가 있고 User에 id 필드가 있으므로, UserId를 user.id 로 인식
    // {동작}{By}{필드1}{And}{필드2}{And}...

    // 같은 유저가 같은 게시물에 이미 좋아요를 눌렀는지 확인
    // 좋아요 중복 검증용
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    // ex: SELECT EXISTS(SELECT 1 FROM post_like WHERE user_id = 1 AND post_id = 5);

    // 특정 좋아요 행 찾기 (좋아요 취소 시 삭제 대상을 가져오기 위함)
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    // ex: SELECT * FROM post_like WHERE user_id = 1 AND post_id = 5;

}
