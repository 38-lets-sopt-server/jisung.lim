package org.sopt.dto.response;

public record ApiResponse<T>(
        int code, // HTTP 상태 코드 (200, 201, 404, 500, ...)
        String message, // 클라에서 확인하는 메시지
        T data // 실제 페이로드. 실패 시 null
) {
    // --성공 응답용 정적 팩토리 메서드--
    // 성공 case 1) 커스텀 코드 + 메시지 + 데이터
    // ex: 201, '게시글 등록 완료', createResponse
    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
    // 성공 case 2) 기본 200 + 기본 메시지 + 데이터
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse(200, "요청이 성공했습니다.", data);
    }

    // --실패 응답용 정적 팩토리 메서드--
    // ex: ApiResponse.error(404, "게시글을 찾을 수 없습니다. id: 999")
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse(code, message, null);
    }
}