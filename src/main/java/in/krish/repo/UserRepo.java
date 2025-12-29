package in.krish.repo;

import in.krish.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByUserIdAndTenantId(Long userId, Long tenantId);

	@Query("""
        SELECT u FROM User u
        WHERE LOWER(u.firstname) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(u.emailid) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
	Page<User> searchUsers(@Param("query") String query, Pageable pageable);
}
