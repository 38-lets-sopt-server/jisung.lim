package org.sopt.domain;

/**
 * 게시판 종류 enum.
 * - FREE: 자유게시판
 * - HOT: HOT 게시판
 * - SECRET: 비밀 게시판
 * <p>
 * 클라이언트는 JSON으로 "FREE" / "HOT" / "SECRET" 문자열을 보내고,
 * Jackson이 자동으로 enum 상수로 변환해준다
 * (잘못된 값이면 Spring이 400 응답 -- GlobalExceptionHandler에서 처리)
 * <p>
 * Post(비즈니스 개념)의 속성이므로 domain에 선언
 */
public enum BoardType {
    FREE,
    HOT,
    SECRET
}
