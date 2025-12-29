package in.krish.service;

import in.krish.binding.PostRequest;
import in.krish.entity.Comment;
import in.krish.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface PostService {

    // ================= POST =================
    Post createPost(
            PostRequest request,
            Long userId,
            Long tenantId,
            String email,
            MultipartFile image
    );

    List<Post> getAllPosts(Long tenantId);

    Post getPostById(Long postId, Long tenantId);

    Post updatePost(
            Long postId,
            String title,
            String content,
            MultipartFile image,
            Long userId,
            Long tenantId,
            Set<String> roles
    );

    void deletePost(
            Long postId,
            Long userId,
            Long tenantId,
            Set<String> roles
    );

    // ================= COMMENTS =================
    Comment addComment(
            Long postId,
            String content,
            Long userId,
            Long tenantId
    );

    List<Comment> getAllCommentsForPost(Long postId, Long tenantId);
}
