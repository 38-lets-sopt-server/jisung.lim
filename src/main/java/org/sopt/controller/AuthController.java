package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.common.SuccessCode;
import org.sopt.dto.request.LoginRequest;
import org.sopt.dto.request.ReissueRequest;
import org.sopt.dto.response.BaseResponse;
import org.sopt.dto.response.TokenResponse;
import org.sopt.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "로그인")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "로그인 성공")})
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.status(SuccessCode.LOGIN_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.LOGIN_SUCCESS, response));
    }

    @Operation(summary = "토큰 재발급",
            description = "Access Token이 만료됐을 때 Refresh Token으로 새 Access/Refresh 토큰을 발급받습니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "재발급 성공")})
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<TokenResponse>> reissue(@RequestBody ReissueRequest request) {
        TokenResponse response = authService.reissue(request);
        return ResponseEntity.status(SuccessCode.REISSUE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.REISSUE_SUCCESS, response));
    }
}
