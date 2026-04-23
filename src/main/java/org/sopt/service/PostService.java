package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service // Bean으로 관리
public class PostService {
    // 생성자에서 주입받을 참조만 선언해둠
    // Spring이 PostService 생성자를 보고 파라미터 타입이 PostRepository, PostValidator임을 확인
    // -> 이미 Bean으로 등록된 인스턴스를 찾아서 자동으로 넣어줌, '생성자 주입'
    // private final -> 불변성 보장 (+: final은 이 주소가 바뀌면 안된다는 뜻이지 객체의 내부는 변경 가능)
    private final PostRepository postRepository;
    private final PostValidator postValidator;

    // 생성자 주입 코드
    // Spring이 PostService 인스턴스 생성 시 Bean에서 PostRepository와 PostValidator 찾아서
    // 생성자의 파라미터에 자동 주입해줌. PostService가 스스로 new X
    public PostService(PostRepository postRepository, PostValidator postValidator) {
        this.postRepository = postRepository;
        this.postValidator = postValidator;
    }

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        // request가 record이므로 request.title()과 같이 필드값 가져옴
        postValidator.validateTitleAndContent(request.title(),
                request.content());
        String createdAt = java.time.LocalDateTime.now().toString();
        Post post = new Post(postRepository.generateId(), request.title(),
                request.content(), request.author(), createdAt);
        postRepository.save(post);
        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
    }

    // READ - 전체
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> responses = new ArrayList<>();
        for (Post post : posts) {
            // new PostResponse(post) -> PostResponse.from(post)로 변경
            responses.add(PostResponse.from(post));
        }
        return responses;
    }

    // READ - 단건
    public PostResponse getPost(Long id) {
        Post post = postValidator.validatePostExists(
                postRepository.findById(id), id);
        return PostResponse.from(post);
    }

    // UPDATE
    public void updatePost(Long id, UpdatePostRequest request) {
        Post post = postValidator.validatePostExists(
                postRepository.findById(id), id);
        postValidator.validateTitleAndContent(request.title(),
                request.content());
        post.update(request.title(), request.content());
    }

    // DELETE
    public void deletePost(Long id) {
        postValidator.validatePostExists(postRepository.findById(id), id);
        postRepository.deleteById(id);
    }
}
