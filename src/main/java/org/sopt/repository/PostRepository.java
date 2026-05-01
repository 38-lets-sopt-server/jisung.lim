package org.sopt.repository;

import org.sopt.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // JpaRepository를 상속하면서 기존 2차과제 코드에서 Repository에 구현했던
    // save(), findById(), deleteById() 등의 CRUD 메서드가 불필요해짐
    // JpaRepository가 기본 CRUD 메서드를 지원해주므로 다 제거
}

/**
 * 2차과제 주석 (JPA 적용 전, Controller와 Service에 DIP 적용한 코드)
 * <p>
 * 게시글 저장소 추상화
 * <p>
 * 상위 모듈(PostService)은 이 인터페이스에만 의존함으로써 DIP를 충족
 * <p>
 * +) 기존에는 PostService가 PostRepository 객체를 직접 참조함
 * PostRepository는 Bean으로 등록되긴 했지만 구체 클래스 그 자체
 * -> 따라서 Spring DI(Dependency Injection)은 적용되지만 SOLID 원칙인 DIP는 적용되지 않음
 * => PostRepository를 interface로 바꾸고 이를 구현한 구현체(InMemoryPostRepository 등)를 선언하고
 * PostService에서는 PostRepository 인터페이스를 참조하도록 하면 DIP 적용 완료!
 * <p>
 * 구현체는 여러 개일 수 있음:
 * - InMemoryPostRepository: ArrayList 기반
 * - DataBasePostRepository: DB 기반(3주차에 적용)
 *
 * @Repository 어노테이션은 인터페이스가 아니라 구현체에 붙음 (인터페이스는 인스턴스화 불가).
 */