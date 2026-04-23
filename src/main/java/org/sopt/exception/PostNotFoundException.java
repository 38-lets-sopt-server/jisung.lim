package org.sopt.exception;

import org.sopt.common.ErrorCode;

// PostNotFoundException도 그냥 BusinessException으로 처리 가능함
// 하지만 중요한 도메인 예외는 별도의 클래스로 둔다고 함(?) -- claude
public class PostNotFoundException extends BusinessException {
    public PostNotFoundException() {
        // 부모(BusinessException) 생성자에 ErrorCode를 넘김
        // BusinessException이 다시 super(errorCode.getMessage())를 실행해
        // BusinessException의 부모(RuntimeException)의 부모(Throwable)의 detailMessage 세팅
        // -> e.getMessage()로 POST_NOT_FOUND enum의 message 필드 접근 가능
        super(ErrorCode.POST_NOT_FOUND);
    }
}
