package org.sopt.common;

import org.springframework.http.HttpStatus;

/**
 * 성공 응답에 사용될 상태코드 + 커스텀코드 + 메시지
 * BaseResponse.success(...) 호출 시 SuccessCode enum 넘겨주기
 * <p>
 * getCode()는 enum 자체의 name()을 반환하므로 커스텀 코드 문자열을 따로 넣어줄 필요 없음.
 * 즉 POST_CREATE_SUCCESS.getCode() == "POST_CREATE_SUCCESS"
 */
public enum SuccessCode {

    // enum 상수에 괄호로 인자 넘기면 -> 그 enum의 생성자 호출
    // -> 컴파일러가 static final 인스턴스 생성으로 번역해주는 문법!
    // 외부에서 접근 시 SuccessCode.POST_LIST_FETCH_SUCCESS와 같이 접근하면 됨
    POST_LIST_FETCH_SUCCESS(HttpStatus.OK, "게시글 목록 조회에 성공했습니다."),
    POST_DETAIL_FETCH_SUCCESS(HttpStatus.OK, "게시글 조회에 성공했습니다."),
    POST_CREATE_SUCCESS(HttpStatus.CREATED, "게시글이 성공적으로 등록되었습니다."),
    POST_UPDATE_SUCCESS(HttpStatus.OK, "게시글이 성공적으로 수정되었습니다."),
    POST_DELETE_SUCCESS(HttpStatus.OK, "게시글이 삭제되었습니다.");

    private final HttpStatus status;
    private final String message;

    SuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    // enum이 자동으로 제공하는 name() 사용 — 상수명을 그대로 String으로 반환
    public String getCode() {
        return name();
    }

    public String getMessage() {
        return message;
    }
}
