package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.exception.PostNotFoundException;

public class PostValidator {

    public void validateTitleAndContent(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }
    }

    public Post validatePostExists(Post post, Long id) {
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        return post;
    }
}
