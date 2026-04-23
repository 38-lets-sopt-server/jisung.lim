package org.sopt.common;

import org.springframework.http.HttpStatus;

/**
 * 성공 응답에 사용될 상태코드 + 커스텀코드 + 메시지
 * ApiResponse.success(...) 호출 시 SuccessCode enum 넘겨주기
 */
public enum SuccessCode {

    // enum 상수에 괄호로 인자 넘기면 -> 그 enum의 생성자 호출
    // -> 컴파일러가 static final 인스턴스 생성으로 번역해주는 문법!
    // 외부에서 접근 시 SuccessCode.POST_LIST_FETCH_SUCCESS와 같이 접근하면 됨
    POST_LIST_FETCH_SUCCESS(HttpStatus.OK, "POST_LIST_FETCH_SUCCESS",
            "게시글 목록 조회에 성공했습니다."), POST_DETAIL_FETCH_SUCCESS(HttpStatus.OK,
            "POST_DETAIL_FETCH_SUCCESS",
            "게시글 조회에 성공했습니다."), POST_CREATE_SUCCESS(HttpStatus.CREATED,
            "POST_CREATE_SUCCESS", "게시글이 성공적으로 등록되었습니다."), POST_UPDATE_SUCCESS(
            HttpStatus.OK, "POST_UPDATE_SUCCESS",
            "게시글이 성공적으로 수정되었습니다."), POST_DELETE_SUCCESS(HttpStatus.OK,
            "POST_DELETE_SUCCESS", "게시글이 삭제되었습니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code; // 서버 커스텀 코드 문자열
    private final String message; // 사용자에게 보여줄 메시지

    SuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ;
}
