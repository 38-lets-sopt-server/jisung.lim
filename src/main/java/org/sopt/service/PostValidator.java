package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.exception.PostNotFoundException;
import org.springframework.stereotype.Component;

// @Repository, @Service, @Controller 등의 어노테이션이 있음
// Validator는 저장소/Service/Controller 모두 아님 -> 범용 Bean으로
// 등록할 때 쓰는 @Component가 적절, 기능은 다른 어노테이션과 동일(Spring Bean으로 등록, 필요한 곳에 주입)
// PostService가 new PostValidator()를 버리고 생성자 주입으로 PostValidator를 받으려면
// PostValidator도 Spring이 관리하는 Bean이어야 함
@Component
public class PostValidator {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 2000;

    public void validateTitleAndContent(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("제목은 " + MAX_TITLE_LENGTH + "자 이하여야 합니다!");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("내용은 " + MAX_CONTENT_LENGTH + "자 이하여야 합니다!");
        }
    }

    public Post validatePostExists(Post post, Long id) {
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        return post;
    }
}
