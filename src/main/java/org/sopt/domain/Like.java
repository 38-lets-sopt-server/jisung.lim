package org.sopt.domain;

import jakarta.persistence.*;

@Entity
// unique: DB에서 중복을 허용하지 않는다
// columnNames = {"user_id", "post_id"}: 두 컬럼의 조합이 unique
// 즉 (user_id, post_id) 쌍이 테이블 전체에서 한 번만 나올 수 있다,
// == 한 user는 같은 post에 좋아요를 한 번만 누를 수 있다
// uk_post_like_user_post: {타입}_{테이블}_{컬럼}
// unique key / post_like / user, post 컬럼
@Table(name = "post_like",
        uniqueConstraints = {@UniqueConstraint(name = "uk_post_like_user_post",
                columnNames = {"user_id", "post_id"})})
public class Like extends BaseTimeEntity {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto_Increment
    private Long id;

    // JPA가 '외래키는 User 엔티티를 가리키는구나' 확인
    // User 엔티티에 @Id가 붙은 PK 필드를 참조 컬럼으로 결정
    // name=user_id로 설정되어 있으므로 Like 테이블의 FK 컬럼 이름을 user_id로 결정,
    //   User 테이블의 PK를 이 테이블의 FK로 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // user_id를 FK로 가짐
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // JPA 리플렉션용 기본 생성자
    protected Like() {
    }

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }
}
