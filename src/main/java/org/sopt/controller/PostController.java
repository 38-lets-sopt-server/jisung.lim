package org.sopt.controller;

import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.service.PostService;

import java.util.List;

public class PostController {
    private final PostService postService = new PostService();

    // POST /posts
    public CreatePostResponse createPost(CreatePostRequest request) {
        try {
            return postService.createPost(request);
        } catch (IllegalArgumentException e) {
            return new CreatePostResponse(null, "🚫 " + e.getMessage());
        }
    }

    // GET /posts 📝 과제
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    // GET /posts/{id} 📝 과제
    public PostResponse getPost(Long id) {
        try {
            return postService.getPost(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // PUT /posts/{id} 📝 과제
    public void updatePost(Long id, String newTitle, String newContent) {
        try {
            postService.updatePost(id, newTitle, newContent);
        } catch (IllegalArgumentException e) {
            System.out.println("🚫 " + e.getMessage());
        }
    }

    // DELETE /posts/{id} 📝 과제
    public void deletePost(Long id) {
        try {
            postService.deletePost(id);
        } catch (IllegalArgumentException e) {
            System.out.println("🚫 " + e.getMessage());
        }
    }
}