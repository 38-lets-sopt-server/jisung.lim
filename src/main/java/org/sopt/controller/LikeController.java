package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.common.SuccessCode;
import org.sopt.dto.request.CreateLikeRequest;
import org.sopt.dto.response.BaseResponse;
import org.sopt.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @Tag: Swagger UI에서 'Like' 그룹으로 묶어서 표시
@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
// 클래스 레벨에 {postId}를 적용하면 클래스 안에 모든 메서드가
// 자동으로 이 path variable 공유, 각 메서드는 추가 path 없이 @PostMapping, @DeleteMapping만 적으면 됨
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "좋아요 추가",
            description = "특정 게시글에 좋아요를 추가합니다. 같은 유저가 같은 게시글에 두 번 누를 수 없습니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "좋아요 추가 성공")})
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> addLike(
            @Parameter(description = "좋아요를 누를 게시글 ID",
                    example = "1",
                    required = true) @PathVariable Long postId,
            @RequestBody CreateLikeRequest request
    ) {
        likeService.addLike(request.userId(), postId);
        return ResponseEntity.status(SuccessCode.POST_LIKE_CREATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_LIKE_CREATE_SUCCESS));
    }

    @Operation(summary = "좋아요 취소", description = "특정 게시글의 좋아요를 취소합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "좋아요 취소 성공")})
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> removeLike(
            @Parameter(description = "좋아요를 취소할 게시글 ID",
                    example = "1",
                    required = true) @PathVariable Long postId,
            @RequestBody CreateLikeRequest request
    ) {
        likeService.removeLike(request.userId(), postId);
        return ResponseEntity.status(SuccessCode.POST_LIKE_DELETE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_LIKE_DELETE_SUCCESS));
    }
}
