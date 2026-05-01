package org.sopt.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// @MappedSuperClass: 이 클래스 자체는 DB 테이블에 매핑되지 않음
//   JPA가 엔티티 저장/수정 이벤트를 감지하면 이 리스너가 작동해서
//   @CreatedDate/@LastModifiedDate가 붙은 필드를 자동으로 채움
//   JpaAuditingConfig에 @EnableJpaAuditing이 켜져있어야 활성화됨
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
// abstract: 이 클래스 자체로 인스턴스를 만들지 않고
// Post, User같은 자식이 상속해서 쓰기 위한 클래스
// Post extends BaseTimeEntity -> createdAt, updatedAt이 자동으로 채워짐
public abstract class BaseTimeEntity {
    // 저장 시점에 자동으로 채워지고, 이후 수정되지 않음
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 저장 시점에는 createdAt과 같은 값으로 채워지고, 이후 수정 시마다 갱신됨
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
