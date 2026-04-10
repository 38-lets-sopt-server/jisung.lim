package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.exception.PostNotFoundException;

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
