package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.common.SuccessCode;
import org.sopt.domain.BoardType;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.BaseResponse;
import org.sopt.dto.response.PageResponse;
import org.sopt.dto.response.PostIdResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts — 인증 필수
    // @AuthenticationPrincipal — JwtAuthFilter가 SecurityContext에 set한 principal(userId) 자동 주입
    @Operation(summary = "게시글 작성", description = "JWT 토큰의 userId로 게시글을 작성합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "게시글 작성 성공")})
    @PostMapping
    public ResponseEntity<BaseResponse<PostIdResponse>> createPost(
            @AuthenticationPrincipal Long userId,
            @RequestBody CreatePostRequest request
    ) {
        PostIdResponse response = postService.createPost(userId, request);
        return ResponseEntity.status(SuccessCode.POST_CREATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_CREATE_SUCCESS, response));
    }

    // GET /posts — 인증 불필요 (SecurityConfig에서 permitAll)
    @Operation(summary = "게시글 목록 조회", description = "페이징된 게시글 목록을 조회합니다. boardType 쿼리 파라미터로 게시판별 필터링이 가능합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공")})
    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<PostResponse>>> getAllPosts(
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "게시판 종류 필터 (FREE / HOT / SECRET). 비우면 전체 조회") @RequestParam(required = false) BoardType boardType
    ) {
        PageResponse<PostResponse> pageData = postService.getAllPosts(page, size, boardType);
        return ResponseEntity.status(SuccessCode.POST_LIST_FETCH_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_LIST_FETCH_SUCCESS, pageData));
    }

    // GET /posts/{id} — 인증 불필요
    @Operation(summary = "게시글 단건 조회", description = "게시글 ID로 특정 게시글을 조회합니다. 삭제된 게시글은 조회되지 않습니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공")})
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(
            @Parameter(description = "조회할 게시글 ID", required = true) @PathVariable Long id
    ) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity.status(SuccessCode.POST_DETAIL_FETCH_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_DETAIL_FETCH_SUCCESS, response));
    }

    // PUT /posts/{id} — 인증 필수 + 본인 글만 수정 가능
    @Operation(summary = "게시글 수정", description = "본인이 작성한 게시글의 제목과 내용을 수정합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "수정 성공")})
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PostIdResponse>> updatePost(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "수정할 게시글 ID", required = true) @PathVariable Long id,
            @RequestBody UpdatePostRequest request
    ) {
        PostIdResponse response = postService.updatePost(userId, id, request);
        return ResponseEntity.status(SuccessCode.POST_UPDATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_UPDATE_SUCCESS, response));
    }

    // DELETE /posts/{id} — 인증 필수 + 본인 글만 삭제 가능
    @Operation(summary = "게시글 삭제 (소프트 딜리트)", description = "본인이 작성한 게시글을 소프트 딜리트합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "삭제 성공")})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PostIdResponse>> deletePost(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "삭제할 게시글 ID", required = true) @PathVariable Long id
    ) {
        PostIdResponse response = postService.deletePost(userId, id);
        return ResponseEntity.status(SuccessCode.POST_DELETE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_DELETE_SUCCESS, response));
    }
}
