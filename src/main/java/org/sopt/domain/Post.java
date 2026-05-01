package org.sopt.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

// 이 클래스를 DB 테이블과 매핑
@Entity
// DB 테이블명 명시적으로 선언, 안쓰면 클래스 이름 그대로 사용
@Table(name = "post")
// postRepository.delete(post) 호출 시 'DELETE FROM post'(기본 DELETE 동작) 대신
// 아래 선언된 UPDATE문을 실행. 에타 게시글 삭제 시 내용을 실제로 지우지 않고 deleted_at 컬럼에 기록만 함
@SQLDelete(sql = "UPDATE post SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
// 모든 SELECT 쿼리의 WHERE 절에 자동으로 아래 조건을 끼워넣음
// -> 존재하는 게시글(DELETED_AT 필드가 없는 게시글)만 리턴함
@SQLRestriction("deleted_at IS NULL")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    // Post는 Many, Author는 One
    // 게시글 조회 시 user는 프록시(가짜 객체)로만 들어오고,
    // getUser.getXxx() 호출 시점에 실제로 DB에서 가져옴 (N+1 방지)
    // post 테이블에 user_id BIGINT 컬럼 자동 생성 + FK 제약조건
    @ManyToOne(fetch = FetchType.LAZY)
    // post 테이블에 user_id라는 FK 컬럼이 생성됨
    // id(PK)는 게시글 식별을 위한 필드, user_id(FK)는 게시글의 작성자 식별을 위한 필드
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @Enumerated(EnumType.String)을 붙여 DB에 'FREE', 'HOT' 같은 문자열로 저장
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BoardType boardType;

    // 소프트 딜리트 표시 컬럼
    // null이면 살아있는 게시글, 값이 있으면 삭제 처리된 게시글
    // 게시글 삭제 시 @SQLDelete(UPDATE ...) 에서 deletedAt 필드 업데이트
    private LocalDateTime deletedAt;

    // BaseTimeEntity 상속했으므로 createdAt, updatedAt 선언도 필요 없음
    // 접근할 때는 BaseTimeEntity에 선언된 getter인 getCreatedAt(), getUpdatedAt()으로 접근 가능

    // JPA 기본 생성자 필요
    protected Post() {
    }

    public Post(String title, String content, User user, BoardType boardType) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.boardType = boardType;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
