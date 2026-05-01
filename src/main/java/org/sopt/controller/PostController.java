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
import org.springframework.web.bind.annotation.*;

// Swagger UI에 띄울 내용
@Tag(name = "Post", description = "게시글 관련 API")
// @RestController = @Controller + @ResponseBody
// 반환 객체를 자동으로 JSON으로 변환해서 HTTP 응답 Body에 실어준다
@RestController // 이 클래스는 REST API 진입점
// 클라가 'HTTP 메서드 + URL'같은 RESTful한 요청(ex: GET /posts)을 보내면
// Spring의 HandlerMapping이 앱 시작 시 @RequestMapping으로 만들어둔 라우팅 테이블('HTTP메서드+URL'에 일치하는 메서드가 key-value로 저장) 조회
// -> 그 조합에 매칭되는 Controller 메서드(ex: getAllPosts)를 호출
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts
    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다. 사전에 등록된 사용자의 userId를 사용합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "게시글 작성 성공")})
    @PostMapping // POST /posts 매핑
    public ResponseEntity<BaseResponse<PostIdResponse>> createPost(@RequestBody CreatePostRequest request) {
        // @RequestBody: 클라로부터 온 HTTP Body의 JSON을 CreatePostRequest 객체로 자동 변환
        // SuccessCode 안에 status + code + message 다 들어있어서 그걸 그대로 꺼내 응답에 세팅
        PostIdResponse response = postService.createPost(request);
        return ResponseEntity.status(SuccessCode.POST_CREATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_CREATE_SUCCESS, response));
    }

    // GET /posts
    @Operation(summary = "게시글 목록 조회", description = "페이징된 게시글 목록을 조회합니다. boardType 쿼리 파라미터로 게시판별 필터링이 가능합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공")})
    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<PostResponse>>> getAllPosts(
            // defaultValue: 클라에서 값 안보내면 이 기본값이 들어가 있는 상태로 진행, null 절대 불가
            // required = false: 클라에서 값 안보내면 null이 들어감
            // 게시판 전체조회 / 특정 게시판 조회를 구분해야하므로 null 가능한게 자연스러움
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "게시판 종류 필터 (FREE / HOT / SECRET). 비우면 전체 조회") @RequestParam(required = false) BoardType boardType
    ) {
        PageResponse<PostResponse> pageData = postService.getAllPosts(page, size, boardType);
        return ResponseEntity.status(SuccessCode.POST_LIST_FETCH_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_LIST_FETCH_SUCCESS, pageData));
    }

    // GET /posts/{id}
    @Operation(summary = "게시글 단건 조회", description = "게시글 ID로 특정 게시글을 조회합니다. 삭제된 게시글은 조회되지 않습니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공")})
    // @PathVariable: URL 경로의 {id} 값을 Long id 파라미터로 주입
    // 없는 아이디면 Service에서 PostNotFoundException 발생 -> GlobalExceptionHandler에서 처리
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(
            @Parameter(description = "조회할 게시글 ID", required = true) @PathVariable Long id
    ) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity.status(SuccessCode.POST_DETAIL_FETCH_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_DETAIL_FETCH_SUCCESS, response));
    }

    // PUT /posts/{id}
    @Operation(summary = "게시글 수정", description = "게시글의 제목과 내용을 수정합니다. 수정된 시각은 자동으로 갱신됩니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "수정 성공")})
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PostIdResponse>> updatePost(
            @Parameter(description = "수정할 게시글 ID", required = true) @PathVariable Long id,
            @RequestBody UpdatePostRequest request
    ) {
        PostIdResponse response = postService.updatePost(id, request);
        return ResponseEntity.status(SuccessCode.POST_UPDATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_UPDATE_SUCCESS, response));
    }

    // DELETE /posts/{id}
    @Operation(summary = "게시글 삭제 (소프트 딜리트)", description = "게시글을 소프트 딜리트합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "삭제 성공")})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<PostIdResponse>> deletePost(
            @Parameter(description = "삭제할 게시글 ID", required = true) @PathVariable Long id
    ) {
        PostIdResponse response = postService.deletePost(id);
        return ResponseEntity.status(SuccessCode.POST_DELETE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_DELETE_SUCCESS, response));
    }
}
