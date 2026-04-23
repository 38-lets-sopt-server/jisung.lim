package org.sopt.repository;

import org.sopt.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * PostRepository 인터페이스의 메모리(ArrayList) 기반 구현체
 *
 * @Repository : Spring이 이 클래스를 Bean으로 등록해줌 -- Spring DI(Dependency Injection)
 * PostService가 PostRepository 타입으로 주입 요청하면 Spring이 이 구현체를 주입
 */
@Repository
public class InMemoryPostRepository implements PostRepository {

    private final List<Post> postList = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public Post save(Post post) {
        postList.add(post);
        return post;
    }

    @Override
    public List<Post> findAll() {
        return postList;
    }

    @Override
    public Post findById(Long id) {
        return postList.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public boolean deleteById(Long id) {
        return postList.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public Long generateId() {
        return nextId++;
    }
}
