package org.sopt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @Configuration : Spring 설정 클래스, 앱 시작 시 Spring이 이 클래스를 읽어들인다
// @EnableJpaAuditing : JPA Auditing 기능을 활성화함
// JPA: 테이블 구조가 아니라 자바 객체(Entity)처럼 코딩할 수 있도록 돕는 API
//   -> @CreatedDate, @LastModifiedDate가 붙은 필드가 엔티티 저장/수정 시점에 자동으로 채워짐
//   -> BaseTimeEntity가 이 기능 사용
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
