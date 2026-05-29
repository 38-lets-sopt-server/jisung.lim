package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.common.SuccessCode;
import org.sopt.dto.response.BaseResponse;
import org.sopt.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 좋아요 추가 — 인증 필수
    // 누른 사람(userId)은 JWT 토큰에서 추출 — body 불필요
    @Operation(summary = "좋아요 추가",
            description = "특정 게시글에 좋아요를 추가합니다. 같은 유저가 같은 게시글에 두 번 누를 수 없습니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "좋아요 추가 성공")})
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> addLike(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "좋아요를 누를 게시글 ID",
                    example = "1",
                    required = true) @PathVariable Long postId
    ) {
        likeService.addLike(userId, postId);
        return ResponseEntity.status(SuccessCode.POST_LIKE_CREATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_LIKE_CREATE_SUCCESS));
    }

    // 좋아요 취소 — 인증 필수
    // 본인이 누른 좋아요만 취소 가능 — findByUserIdAndPostId에서 본인 row 못 찾으면 자동으로 LIKE_NOT_FOUND
    @Operation(summary = "좋아요 취소", description = "본인이 누른 좋아요를 취소합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "좋아요 취소 성공")})
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> removeLike(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "좋아요를 취소할 게시글 ID",
                    example = "1",
                    required = true) @PathVariable Long postId
    ) {
        likeService.removeLike(userId, postId);
        return ResponseEntity.status(SuccessCode.POST_LIKE_DELETE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_LIKE_DELETE_SUCCESS));
    }
}
