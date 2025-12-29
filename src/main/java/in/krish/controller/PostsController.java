package in.krish.controller;

import in.krish.binding.*;
import in.krish.entity.Comment;
import in.krish.entity.FeedEntry;
import in.krish.entity.Post;
import in.krish.impl.PostServiceImpl;
import in.krish.repo.FeedRepository;
import in.krish.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PostsController {

    @Autowired
    private PostService postService;

    @Autowired
    private FeedRepository feedRepo;

    // ================= CREATE POST =================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @ModelAttribute PostRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
            @RequestHeader(value = "X-User-Email", required = false) String email
    ) {
        Post post = postService.createPost(request, userId, tenantId, email, image);
        return ResponseEntity.ok(new ApiResponse<>(200, "Post created", new PostDTO(post)));
    }

    // ================= READ =================
    @GetMapping
    public ApiResponse<List<PostDTO>> getAllPosts(
            @RequestHeader("X-Tenant-Id") Long tenantId
    ) {
        return new ApiResponse<>(
                200,
                "OK",
                postService.getAllPosts(tenantId)
                        .stream()
                        .map(PostDTO::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<PostDTO> getPostById(
            @PathVariable Long id,
            @RequestHeader("X-Tenant-Id") Long tenantId
    ) {
        return new ApiResponse<>(
                200,
                "OK",
                new PostDTO(postService.getPostById(id, tenantId))
        );
    }

    // ================= UPDATE =================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostDTO> updatePost(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @RequestHeader("X-User-Roles") String roles
    ) {
        Set<String> roleSet = Set.of(roles.split(","));
        Post post = postService.updatePost(
                id, title, content, image, userId, tenantId, roleSet
        );
        return new ApiResponse<>(200, "Updated", new PostDTO(post));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @RequestHeader("X-User-Roles") String roles
    ) {
        Set<String> roleSet = Set.of(roles.split(","));
        postService.deletePost(id, userId, tenantId, roleSet);
        return ResponseEntity.ok().build();
    }

    // ================= COMMENTS =================
    @PostMapping("/{postId}/comment")
    public ApiResponse<CommentResponse> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest req,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-Tenant-Id") Long tenantId
    ) {
        Comment c = postService.addComment(postId, req.getContent(), userId, tenantId);
        return new ApiResponse<>(200, "Comment added", new CommentResponse(c));
    }

    @GetMapping("/feeds")
    public ResponseEntity<List<FeedEntry>> feeds(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(
                feedRepo.findByUserIdOrderByCreatedAtDesc(userId)
        );
    }


    // ================= PUBLIC =================
    @GetMapping("/quote")
    public ResponseEntity<String> quote() {
        return ResponseEntity.ok(
                new RestTemplate()
                        .getForObject("https://zenquotes.io/api/today", String.class)
        );
    }
}
