package org.sopt.dto.response;

import org.sopt.common.ErrorCode;
import org.sopt.common.SuccessCode;

/**
 * 모든 API 응답을 감싸는 공통 응답 wrapper
 * <p>
 * status  — HTTP 상태 코드 (200, 201, 404, 500 등)
 * code    — 서버 커스텀 코드 (POST_NOT_FOUND, POST_CREATE_SUCCESS 등)
 * message — 사용자에게 보여줄 메시지
 * data    — 실제 페이로드. 에러 응답 시 null
 * <p>
 * 성공/실패 시 SuccessCode / ErrorCode enum만 넘겨주면
 * status, code, message가 한꺼번에 세팅됨
 */
public record ApiResponse<T>(int status, String code, String message, T data) {

    // --- 성공 응답용 정적 팩토리 ---
    // enum + 데이터
    // ex: ApiResponse.success(SuccessCode.POST_DETAIL_FETCH_SUCCESS, postResponse)
    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<>(
                successCode.getStatus().value(),
                successCode.getCode(),
                successCode.getMessage(),
                data
        );
    }

    // 데이터가 없는 성공 응답 (수정/삭제 등)
    // ex: ApiResponse.success(SuccessCode.POST_DELETE_SUCCESS)
    public static <T> ApiResponse<T> success(SuccessCode successCode) {
        return new ApiResponse<>(
                successCode.getStatus().value(),
                successCode.getCode(),
                successCode.getMessage(),
                null
        );
    }

    // --- 실패 응답용 정적 팩토리 ---
    // ex: ApiResponse.error(ErrorCode.POST_NOT_FOUND)
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(
                errorCode.getStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                null
        );
    }
}
