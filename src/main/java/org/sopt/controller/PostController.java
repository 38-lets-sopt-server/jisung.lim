package org.sopt.controller;

import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.PostNotFoundException;
import org.sopt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// RestController = Controller + ResponseBody 합본
// 반환 객체를 자동으로 JSON으로 변환해서 HTTP 응답 Body에 실어준다
@RestController // 이 클래스는 REST API 진입점
@RequestMapping("/posts") // 모든 메서드는 URL 앞에 /posts 자동으로 붙임
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts
    // @RequestBody: 클라로부터 온 HTTP Body의 JSON을 CreatePostRequest 객체로 자동 변환
    // ResponseEntity<T>: 상태 코드(201 Created)와 Body를 함께 반환
    @PostMapping // POST /posts 매핑
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /posts
    // 성공 시 200 OK + 게시글 리스트 반환
    @GetMapping // /posts
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> responses = postService.getAllPosts();
        return ResponseEntity.ok(responses);
    }

    // GET /posts/{id}
    // @PathVariable: URL 경로의 {id} 값을 Long id 파라미터로 주입
    // 없는 아이디면 Service에서 PostNotFoundException 발생 -> GlobalExceptionHandler에서 처리
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity.ok(response);
    }

    // PUT /posts/{id}
    // 성공 시 204 No Content (응답 Body 없음이 관례)
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest request) {
        postService.updatePost(id, request);
        return ResponseEntity.noContent().build();
    }

    // DELETE /posts/{id}
    // 성공 시 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}