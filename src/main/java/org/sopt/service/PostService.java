package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

public class PostService {
    private final PostRepository postRepository = new PostRepository();
    private final PostValidator postValidator = new PostValidator();

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        postValidator.validateTitleAndContent(request.title, request.content);
        String createdAt = java.time.LocalDateTime.now().toString();
        Post post = new Post(postRepository.generateId(), request.title, request.content, request.author, createdAt);
        postRepository.save(post);
        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
    }

    // READ - 전체
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> responses = new ArrayList<>();
        for (Post post : posts) {
            responses.add(new PostResponse(post));
        }
        return responses;
    }

    // READ - 단건
    public PostResponse getPost(Long id) {
        Post post = postValidator.validatePostExists(postRepository.findById(id), id);
        return new PostResponse(post);
    }

    // UPDATE
    public void updatePost(Long id, String newTitle, String newContent) {
        Post post = postValidator.validatePostExists(postRepository.findById(id), id);
        postValidator.validateTitleAndContent(newTitle, newContent);
        post.update(newTitle, newContent);
    }

    // DELETE
    public void deletePost(Long id) {
        postValidator.validatePostExists(postRepository.findById(id), id);
        postRepository.deleteById(id);
    }
}
