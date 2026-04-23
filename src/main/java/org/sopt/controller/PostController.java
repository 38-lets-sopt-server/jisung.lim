package org.sopt.controller;

import org.sopt.common.SuccessCode;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.ApiResponse;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.service.PostService;
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
    // SuccessCode 안에 status + code + message 다 들어있어서 그걸 그대로 꺼내 응답에 세팅
    @PostMapping // POST /posts 매핑
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(@RequestBody CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request);
        return ResponseEntity
                .status(SuccessCode.POST_CREATE_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_CREATE_SUCCESS, response));
    }

    // GET /posts
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> responses = postService.getAllPosts();
        return ResponseEntity
                .status(SuccessCode.POST_LIST_FETCH_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_LIST_FETCH_SUCCESS, responses));
    }

    // GET /posts/{id}
    // @PathVariable: URL 경로의 {id} 값을 Long id 파라미터로 주입
    // 없는 아이디면 Service에서 PostNotFoundException 발생 -> GlobalExceptionHandler에서 처리
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity
                .status(SuccessCode.POST_DETAIL_FETCH_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_DETAIL_FETCH_SUCCESS, response));
    }

    // PUT /posts/{id}
    // 반환할 페이로드가 없으므로 data=null 오버로드 사용
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest request) {
        postService.updatePost(id, request);
        return ResponseEntity
                .status(SuccessCode.POST_UPDATE_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_UPDATE_SUCCESS));
    }

    // DELETE /posts/{id}
    // 위와 같은 이유로 data=null
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity
                .status(SuccessCode.POST_DELETE_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_DELETE_SUCCESS));
    }
}
