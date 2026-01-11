package in.krish.impl;

import in.krish.binding.PostCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import in.krish.binding.PostRequest;
import in.krish.entity.*;
import in.krish.exception.PostNotFoundException;
import in.krish.repo.*;
import in.krish.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private FeedRepository feedRepo;

    @Autowired
    private FollowerRepo followerRepo;

    private final KafkaTemplate<String, PostCreatedEvent> kafkaTemplate;


    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public PostServiceImpl(KafkaTemplate<String, PostCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // =====================================================
    // CREATE POST
    // =====================================================
    @Override
    public Post createPost(
            PostRequest request,
            Long userId,
            Long tenantId,
            String email,
            MultipartFile image
    ) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUserId(userId);      // ✅ store ID only
        post.setTenantId(tenantId);

        if (image != null && !image.isEmpty()) {
            post.setImageUrl(saveImage(image));
        }

        Post savedPost = postRepo.save(post);

        // PUBLISH EVENT
        kafkaTemplate.send(
                "post-events",
                new PostCreatedEvent(
                        savedPost.getId(),
                        userId,
                        tenantId,
                        savedPost.getTitle()
                )
        );

        return savedPost;
    }

    // =====================================================
    // READ
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(Long tenantId) {
        return postRepo.findByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long postId, Long tenantId) {
        return postRepo.findByIdAndTenantId(postId, tenantId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // =====================================================
    // UPDATE (OWNER OR ADMIN)
    // =====================================================
    @Override
    public Post updatePost(
            Long postId,
            String title,
            String content,
            MultipartFile image,
            Long userId,
            Long tenantId,
            Set<String> roles
    ) {
        Post post = postRepo.findByIdAndTenantId(postId, tenantId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean isOwner = post.getUserId().equals(userId);
        boolean isAdmin = roles != null && roles.contains("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Unauthorized to update post");
        }

        post.setTitle(title);
        post.setContent(content);

        if (image != null && !image.isEmpty()) {
            deleteOldImage(post.getImageUrl());
            post.setImageUrl(saveImage(image));
        }

        return postRepo.save(post);
    }

    // =====================================================
    // DELETE (OWNER OR ADMIN)
    // =====================================================
    @Override
    public void deletePost(
            Long postId,
            Long userId,
            Long tenantId,
            Set<String> roles
    ) {
        Post post = postRepo.findByIdAndTenantId(postId, tenantId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        boolean isOwner = post.getUserId().equals(userId);
        boolean isAdmin = roles != null && roles.contains("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You are not allowed to delete this post");
        }

        deleteOldImage(post.getImageUrl());
        postRepo.delete(post);
    }


    // =====================================================
    // COMMENTS
    // =====================================================
    @Override
    public Comment addComment(
            Long postId,
            String content,
            Long userId,
            Long tenantId
    ) {
        Post post = postRepo.findByIdAndTenantId(postId, tenantId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(userId);   // ✅ ID only
        comment.setContent(content);
        comment.setTenantId(tenantId);

        return commentRepo.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllCommentsForPost(Long postId, Long tenantId) {
        return commentRepo.findByPostIdAndTenantId(postId, tenantId);
    }

    // =====================================================
    // HELPERS
    // =====================================================
    private String saveImage(MultipartFile image) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            try (InputStream in = image.getInputStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private void deleteOldImage(String imageUrl) {
        if (imageUrl == null) return;
        try {
            Path path = Paths.get(uploadDir, imageUrl.replace("/uploads/", ""));
            Files.deleteIfExists(path);
        } catch (IOException ignored) {}
    }

}

