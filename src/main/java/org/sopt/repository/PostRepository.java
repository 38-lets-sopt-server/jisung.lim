package org.sopt.repository;

import org.sopt.domain.Post;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

// @Repository -> Spring이 이 클래스를 Bean으로 등록하고 관리함
// 앱 시작 시 Spring이 PostRepository 인스턴스를 만들어 Container에 보관
// 이후 PostService 등이 이 인스턴스를 주입받아 사용
@Repository
public class PostRepository {
    private final List<Post> postList = new ArrayList<>();
    private Long nextId = 1L;

    public Post save(Post post) {
        postList.add(post);
        return post;
    }

    public List<Post> findAll() {
        return postList;
    }

    public Post findById(Long id) {
        return postList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteById(Long id) {
        return postList.removeIf(p -> p.getId().equals(id));
    }

    public Long generateId() {
        return nextId++;
    }
}