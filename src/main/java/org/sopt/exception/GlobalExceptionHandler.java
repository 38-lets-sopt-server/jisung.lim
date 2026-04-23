package org.sopt.exception;

import org.sopt.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice:
// 모든 @RestController에서 발생한 예외를 감시하는 '전역 예외 처리자'
// 여기 정의한 @ExceptionHandler 메서드가 해당 예외를 가로채서 응답으로 변환.
// (@ControllerAdvice + @ResponseBody라서 반환값이 JSON으로 자동 변환됨)
// +) 에러 응답이나 update/delete 성공 응답은 반환할 데이터가 없음 ->
// ApiResponse<Void>로 Void 타입을 사용해 ApiResponse의 data 필드가 비어있음을 알려줌
@RestControllerAdvice
public class GlobalExceptionHandler {
    // PostNotFoundException 발생 시 호출
    // Service에서 "없는 id로 조회/수정/삭제 시도"했을 때 이 핸들러가 작동
    // → 404 Not Found + 에러 메시지 반환
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostNotFound(PostNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, e.getMessage()));
    }

    // IllegalArgumentException 발생 시 호출
    // PostValidator가 제목/내용 검증 실패 시 던지는 예외
    // → 400 Bad Request (클라이언트가 잘못된 요청을 보냄).
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    // 위에서 안 잡힌 모든 예외의 마지막 안전망
    // Spring은 더 구체적인 핸들러를 우선 매칭하므로,
    // 이 핸들러는 "예상 못한 버그나 시스템 오류"가 터졌을 때만 실행됨
    // → 500 Internal Server Error + 일반화된 메시지
    //   (보안상 e.getMessage()를 그대로 노출하지 않고 일반 메시지 반환)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "서버 내부 오류가 발생했습니다."));
    }
}
