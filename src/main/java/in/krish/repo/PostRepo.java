package in.krish.repo;

//import in.krish.binding.PostSummaryDto;
import in.krish.entity.Post;
import in.krish.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);

    List<Post> findByTenantId(Long tenantId);

    Optional<Post> findByIdAndTenantId(Long postId, Long tenantId);

    List<Post> findByUserIdAndTenantId(Long userId, Long tenantId);
}
