package in.krish.configuration;

import in.krish.entity.Post;
import in.krish.repo.PostRepo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthorizationService {

    private final PostRepo postRepo;

    public AuthorizationService(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    public void checkPostOwnerOrAdmin(
            Long postId,
            Long userId,
            Long tenantId,
            Set<String> roles
    ) {
        Post post = postRepo.findByIdAndTenantId(postId, tenantId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean isOwner = post.getUserId().equals(userId);
        boolean isAdmin = roles != null && roles.contains("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Unauthorized");
        }
    }
}
