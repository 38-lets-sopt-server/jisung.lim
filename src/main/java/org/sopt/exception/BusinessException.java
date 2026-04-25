package org.sopt.exception;

import org.sopt.common.ErrorCode;

// Java의 기본 IllegalArgumentException(1주차 커스텀 에러의 부모)에는 ErrorCode를 담을 공간이 없음
// 따라서 GlobalExceptionHandler의 @ExceptionHandler에서 /common/ErrorCode의 ErrorCode를 받아
// 적절한 에러 형식(status, code, message)을 구현해 클라에게 넘겨주려면 커스텀 에러를 담는 별도의 클래스 필요
public class BusinessException extends RuntimeException {

    // 서버 커스텀 에러코드(status, code, message)를 담는 필드
    // -> BusinessException은 RuntimeException의 message 필드와 자신의 errorCode 필드를 가지는 클래스가 됨
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        // RuntimeException의 부모인 Throwable 내부에 detailMessage 필드가 있는데,
        // 명시적으로 super()에 메시지를 전달하지 않으면 detailMessage가 null이 되어
        // e.getMessage()의 결과가 null이 된다
        super(errorCode.getMessage()); // e.message() 호출 대비 -> RuntimeException의 메시지도 ErrorCode에서 뽑아둠
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
