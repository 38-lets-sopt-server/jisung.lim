package org.sopt.service;

import org.sopt.common.ErrorCode;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.exception.BusinessException;
import org.sopt.repository.LikeRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(
            LikeRepository likeRepository,
            UserRepository userRepository,
            PostRepository postRepository
    ) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 좋아요 추가
    @Transactional
    public void addLike(Long userId, Long postId) {
        // 1. 사용자 존재 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        // 2. 게시글 존재 검증
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        // 3. 중복 좋아요 검증
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
        }
        // 4. LIKE 엔티티 생성 및 저장
        likeRepository.save(new Like(user, post));
    }

    // 좋아요 취소
    @Transactional
    public void removeLike(Long userId, Long postId) {
        // findByUserIdAndPostId가 Optional<Like> 반환
        // like가 없으면 .orElseThrow()에서 LIKE_NOT_FOUND 반환
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

        // like가 있으면 찾은 Like 엔티티를 likeRepository.delete()로 삭제
        likeRepository.delete(like);
    }
}
