package org.sopt.controller;

import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.ApiResponse;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = @Controller + @ResponseBody
// 반환 객체를 자동으로 JSON으로 변환해서 HTTP 응답 Body에 실어준다
@RestController // 이 클래스는 REST API 진입점
// 클라가 'HTTP 메서드 + URL'같은 RESTful한 요청(ex: GET /posts)을 보내면
// Spring의 HandlerMapping이 앱 시작 시 @RequestMapping으로 만들어둔 라우팅 테이블('HTTP메서드+URL'에 일치하는 메서드가 key-value로 저장) 조회
// -> 그 조합에 매칭되는 Controller 메서드(ex: getAllPosts)를 호출
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts
    // @RequestBody: 클라로부터 온 HTTP Body의 JSON을 CreatePostRequest 객체로 자동 변환
    // ResponseEntity<T>: 상태 코드(201 Created)와 Body를 함께 반환
    @PostMapping // POST /posts 매핑
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(@RequestBody CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request);
        // ResponseEntity.status(): 원하는 상태 코드 지정 가능
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "게시글 등록 완료!", response));
    }

    // GET /posts
    // 성공 시 200 OK + 게시글 리스트를 ApiResponse로 감싸서 반환
    @GetMapping // /posts
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> responses = postService.getAllPosts();
        // ResponseEntity.ok(): .status(HTTPStatus.OK)의 단축형, 200 전용
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    // GET /posts/{id}
    // @PathVariable: URL 경로의 {id} 값을 Long id 파라미터로 주입
    // 없는 아이디면 Service에서 PostNotFoundException 발생 -> GlobalExceptionHandler에서 처리
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // PUT /posts/{id}
    // 반환할 페이로드가 없으므로 data는 null, 타입은 Void.
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest request) {
        postService.updatePost(id, request);
        return ResponseEntity.ok(ApiResponse.success(200, "게시글 수정 완료", null));
    }

    // DELETE /posts/{id}
    // 위와 같은 이유로 200 OK + data=null.
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success(200, "게시글 삭제 완료", null));
    }
}