package org.sopt.repository;

import org.sopt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 인터페이스가 JpaRepository를 상속하면 Spring Data JPA가
// 자동으로 Bean으로 등록해주므로 @Repository는 필수 X, 관례상 붙이는 것도 허용
// extends JpaRepository: save, findById, count, delete, 페이지네이션 등
//   기본 CRUD 메서드 + a 다양하게 제공
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
