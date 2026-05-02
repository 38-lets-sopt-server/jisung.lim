package org.sopt.domain;

import jakarta.persistence.*;

// @Entity: 이 클래스를 DB 테이블과 매핑, JPA가 이 클래스를 엔티티로 관리하기 시작
// @Table(name = "users"): 'user'는 MySQL의 SQL 예약어 -> 테이블명을 'users'로 저장해 충돌 피함
@Entity
@Table(name = "users")
// createdAt, updatedAt 필드를 JPA가 자동 관리
public class User extends BaseTimeEntity {

    // 이 필드가 primary key
    @Id
    // MySQL의 AUTO_INCREMENT로 DB가 id를 자동 부여
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // JPA는 기본 생성자 반드시 필요
    // protected: JPA는 사용 가능, 외부 코드는 new User()로 빈 객체 만들 수 없음
    protected User() {
    }

    public User(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
}
