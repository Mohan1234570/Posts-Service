package in.krish.repo;

import in.krish.entity.FeedEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<FeedEntry, Long> {

    List<FeedEntry> findByUserIdOrderByCreatedAtDesc(Long userId);
}
