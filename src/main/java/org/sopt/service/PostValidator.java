package org.sopt.service;

import org.sopt.common.ErrorCode;
import org.sopt.exception.BusinessException;
import org.springframework.stereotype.Component;

// @Component: 범용 Spring Bean으로 등록
//   Repository/Service/Controller 어디에도 속하지 않는 보조 컴포넌트라 @Component 사용
//   기능은 다른 어노테이션과 동일(Bean 등록 + DI 대상)
//
// PostService가 new PostValidator()를 버리고 생성자 주입으로 PostValidator를 받으려면
// PostValidator도 Spring이 관리하는 Bean이어야 함
@Component
public class PostValidator {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 2000;

    public void validateTitleAndContent(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new BusinessException(ErrorCode.TITLE_REQUIRED);
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new BusinessException(ErrorCode.TITLE_TOO_LONG);
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new BusinessException(ErrorCode.CONTENT_TOO_LONG);
        }
    }
}