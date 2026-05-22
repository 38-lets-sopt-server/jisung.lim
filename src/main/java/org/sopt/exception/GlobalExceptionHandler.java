package org.sopt.exception;

import org.sopt.common.ErrorCode;
import org.sopt.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// @RestControllerAdvice:
// 모든 @RestController에서 발생한 예외를 감시하는 '전역 예외 처리자'
// 여기 정의한 @ExceptionHandler 메서드가 해당 예외를 가로채서 응답으로 변환.
// (@ControllerAdvice + @ResponseBody라서 반환값이 JSON으로 자동 변환됨)
// +) 에러 응답이나 update/delete 성공 응답은 반환할 데이터가 없음 ->
// BaseResponse<Void>로 Void 타입을 사용해 BaseResponse의 data 필드가 비어있음을 알려줌
@RestControllerAdvice
public class GlobalExceptionHandler {
    // BusinessException + 그 자식(PostNotFoundException 등) 전부 여기서 처리
    // 예외가 들고 있는 ErrorCode를 꺼내 상태/코드/메시지 일괄 세팅 -> 클라에 반환할 형식으로 변환해 반환
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        // 상태 코드는 ErrorCode가 갖고 있는 값 사용 (POST_NOT_FOUND면 404, TITLE_REQUIRED면 400 등)
        return ResponseEntity.status(errorCode.getStatus()).body(BaseResponse.error(errorCode));
    }

    // Jackson이 HTTP Body 파싱 실패 시 발생
    // JSON 문법 자체가 깨짐, 잘못된 enum값으로 Json -> Java 객체변환 실패, 필수 필드 누락, 타입 불일치
    // ex: 잘못된 BoardType 전달 -> Java enum 객체로 변환 실패 -> 400 Bad Request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Void>> handleMessageNotReadable(
            HttpMessageNotReadableException e
    ) {
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus())
                .body(BaseResponse.error(ErrorCode.BAD_REQUEST));
    }

    // HTTP Body 파싱 실패가 아니라, Query/Path 변수 타입 변환 실패
    // @RequestParam, @PathVariable에 잘못된 값이 전달됐을 때 발생
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus())
                .body(BaseResponse.error(ErrorCode.BAD_REQUEST));
    }

    // 위에서 안 잡힌 모든 예외의 마지막 안전망
    // Spring은 더 구체적인 핸들러를 우선 매칭하므로,
    // 이 핸들러는 "예상 못한 버그나 시스템 오류"가 터졌을 때만 실행됨
    // → 500 Internal Server Error + 일반화된 메시지
    // (보안상 e.getMessage()를 그대로 노출하지 않고 일반 메시지 반환)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
        // 임시 디버깅용
        e.printStackTrace();
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(BaseResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
