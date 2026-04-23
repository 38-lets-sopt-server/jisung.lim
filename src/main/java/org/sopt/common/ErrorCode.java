package org.sopt.common;

import org.springframework.http.HttpStatus;

/**
 * 에러 응답에 사용될 상태 코드 + 커스텀 코드 + 메시지 모음
 * 예외를 던지거나 GlobalExceptionHandler에서 응답 조립할 때 사용.
 *
 * getCode()는 enum 자체의 name()을 반환하므로 커스텀 코드 문자열을 따로 넣어줄 필요 없음.
 * 즉 TITLE_REQUIRED.getCode() == "TITLE_REQUIRED"
 */
public enum ErrorCode {

    // 400 Bad Request — 입력 검증 실패
    TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),
    TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "제목은 50자 이하여야 합니다."),
    CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "내용은 2000자 이하여야 합니다."),
    TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST, "이미지는 최대 10개까지 첨부 가능합니다."),
    INVALID_PAGINATION_PARAMETER(HttpStatus.BAD_REQUEST, "페이지 파라미터가 유효하지 않습니다. (page: 0 이상, size: 1~50)"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 파라미터를 확인해주세요."),

    // 401 Unauthorized — 인증 실패
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),

    // 403 Forbidden — 권한 없음
    FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "본인이 작성한 글만 삭제할 수 있습니다."),
    FORBIDDEN_UPDATE(HttpStatus.FORBIDDEN, "본인이 작성한 글만 수정할 수 있습니다."),

    // 404 Not Found — 리소스 없음
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."),

    // 500 Internal Server Error — 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 이용이 일시적으로 원활하지 않습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
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
