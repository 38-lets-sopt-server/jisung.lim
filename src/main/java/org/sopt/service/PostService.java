package org.sopt.service;

import org.sopt.common.ErrorCode;
import org.sopt.domain.BoardType;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.PageResponse;
import org.sopt.dto.response.PostIdResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.BusinessException;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostValidator postValidator;
    private final UserRepository userRepository;

    public PostService(
            PostRepository postRepository,
            PostValidator postValidator,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.postValidator = postValidator;
        this.userRepository = userRepository;
    }

    // CREATE
    // userId는 컨트롤러에서 @AuthenticationPrincipal로 받아온 JWT의 userId
    // 더 이상 CreatePostRequest의 userId 필드 사용 X
    @Transactional
    public PostIdResponse createPost(Long userId, CreatePostRequest request) {
        postValidator.validateTitleAndContent(request.title(), request.content());

        // 토큰의 userId로 User 조회 — 정상 토큰이면 항상 존재해야 함
        // +) 그래도 방어 차원에서 USER_NOT_FOUND 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = new Post(request.title(), request.content(), user, request.boardType());
        postRepository.save(post);

        return new PostIdResponse(post.getId());
    }

    // READ - 전체
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> getAllPosts(int page, int size, BoardType boardType) {
        List<Post> all = postRepository.findAllWithUserAndLikes();

        List<Post> filtered = all.stream()
                .filter(p -> boardType == null || p.getBoardType() == boardType)
                .toList();

        int from = page * size;
        int to = Math.min(from + size, filtered.size());
        List<Post> pageSlice = from >= filtered.size() ? List.of() : filtered.subList(from, to);

        List<PostResponse> content = pageSlice.stream().map(PostResponse::from).toList();
        boolean hasNext = to < filtered.size();

        return new PageResponse<PostResponse>(content, page, size, hasNext);
    }

    // READ - 단건
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return PostResponse.from(post);
    }

    // UPDATE — 본인 글만 수정 가능
    // 더티 체킹: 영속성 컨텍스트 안에 있는 엔티티를 수정하면 트랜잭션 커밋 시
    // 자동으로 UPDATE 쿼리가 나감, repository.save(post) 명시적으로 호출할 필요 X
    @Transactional
    public PostIdResponse updatePost(Long userId, Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        // 본인 글 검증 — 토큰의 userId !== 게시글 작성자 userId면 403
        if (!post.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_UPDATE);
        }

        postValidator.validateTitleAndContent(request.title(), request.content());
        post.update(request.title(), request.content());

        return new PostIdResponse(id);
    }

    // DELETE — 본인 글만 삭제 가능
    @Transactional
    public PostIdResponse deletePost(Long userId, Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        // 본인 글 검증 — 다른 사람 글 삭제 시도 시 403
        if (!post.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_DELETE);
        }

        postRepository.delete(post);
        return new PostIdResponse(id);
    }
}
