package org.sopt.service;

import org.sopt.common.ErrorCode;
import org.sopt.domain.BoardType;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PageResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.BusinessException;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Bean으로 관리
public class PostService {
    // 생성자에서 주입받을 참조만 선언해둠
    // Spring이 PostService 생성자를 보고 파라미터 타입이 PostRepository, PostValidator임을 확인
    // -> 이미 Bean으로 등록된 인스턴스를 찾아서 자동으로 넣어줌, '생성자 주입'
    // private final -> 불변성 보장 (+: final은 이 주소가 바뀌면 안된다는 뜻이지 객체의 내부는 변경 가능)
    private final PostRepository postRepository;
    private final PostValidator postValidator;
    private final UserRepository userRepository;

    // 생성자 주입 코드
    // Spring이 PostService 인스턴스 생성 시 Bean에서 PostRepository와 PostValidator 찾아서
    // 생성자의 파라미터에 자동 주입해줌. PostService가 스스로 new X
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
    // @Transactional: 메서드 시작 시 트랜잭션이 열리고 정상 종료 시 커밋됨
    //   영속성 컨텍스트가 열려있어 더티 체킹과 LAZY 로딩 정상 동작
    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {
        // 입력값 검증 (제목/본문 길이)
        postValidator.validateTitleAndContent(request.title(), request.content());

        // userId로 User 엔티티 조회, 없으면 USER_NOT_FOUND 예외
        // JpaRepository의 findById()는 Optional을 반환하므로 에러 throw해야함
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 새 post 생성 (id와 createdAt은 JPA가 자동 처리)
        Post post = new Post(request.title(), request.content(), user, request.boardType());
        postRepository.save(post);

        return new CreatePostResponse(post.getId());
    }

    // READ - 전체
    // readOnly = true -> 트랜잭션을 읽기 전용으로 표시, 더티 체킹 건너뛰어 약간의 성능 이점
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> getAllPosts(int page, int size, BoardType boardType) {
        // 1. 전체 게시글 가져오기
        List<Post> all = postRepository.findAll();

        // 2. boardType 필터링 (null이면 전체, 아니면 일치하는 게시글만)
        // boardType == null이면 filter() 조건이 true가 되어 전체 게시글 반환
        // boardType == 'HOT'이면 boardType == null가 false이므로 p.getBoardType() == boardType을 만족하는
        // 리스트만 반환
        List<Post> filtered = all.stream()
                .filter(p -> boardType == null || p.getBoardType() == boardType)
                .toList();

        // 3. 페이지 슬라이싱
        int from = page * size;
        int to = Math.min(from + size, filtered.size());
        List<Post> pageSlice = from >= filtered.size() ? List.of() : filtered.subList(from, to);

        // 4. DTO 반환
        List<PostResponse> content = pageSlice.stream().map(PostResponse::from).toList();

        // 5. hasNext 계산 (다음 페이지 존재 여부)
        boolean hasNext = to < filtered.size();

        // 6. PageResponse 생성
        // 요청/응답 순간에만 존재, 불변(상태 없음), 매 요청마다 다른 데이터로 새로 만들어지는게 정상, Spring이 관리할 필요 없음
        // => Spring DI의 관리 대상 X, new 사용하는게 당연함
        return new PageResponse<PostResponse>(content, page, size, hasNext);
    }

    // READ - 단건
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        // PostNotFoundException::new == 람다를 짧게 쓴 것 == () => new PostNotFoundException()
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return PostResponse.from(post);
    }

    // UPDATE
    // 더티 체킹: 영속성 컨텍스트 안에 있는 엔티티를 수정하면 트랜잭션 커밋 시
    // 자동으로 UPDATE 쿼리가 나감, repository.save(post) 명시적으로 호출할 필요 X
    @Transactional
    public void updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        postValidator.validateTitleAndContent(request.title(), request.content());
        post.update(request.title(), request.content());

        // 별도의 save() 호출 없음, 트랜잭션 커밋 시점에 자동 UPDATE
    }

    // DELETE
    // postRepository.delete(post) 호출 시 Post 엔티티의 @SQLDelete에 적용한 쿼리가 실행되어
    // 실제 DELETE가 아니라 'UPDATE post SET ...' 구문이 실행됨 -> 해당 post에 deleted_at 필드가 추가됨
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
    }
}
