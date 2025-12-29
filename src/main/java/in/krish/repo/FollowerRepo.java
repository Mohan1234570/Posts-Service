//package in.krish.repo;
//
//import in.krish.entity.Follower;
//import in.krish.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//public interface FollowerRepo extends JpaRepository<Follower, Long> {
//
//    boolean existsByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);
//
//    @Query("SELECT COUNT(f) FROM Follower f WHERE f.following.userId = :userId")
//    long countFollowers(@Param("userId") Long userId);
//
//    @Query("SELECT COUNT(f) FROM Follower f WHERE f.follower.userId = :userId")
//    long countFollowing(@Param("userId") Long userId);
//
//    List<Follower> findByFollowing_UserId(Long userId);
//
//    List<Follower> findByFollower_UserId(Long userId);
//
//    void deleteByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);
//}

package in.krish.repo;


import in.krish.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerRepo extends JpaRepository<Follower, Long> {

    boolean existsByFollower_UserIdAndFollowing_UserIdAndTenantId(
            Long followerId, Long followingId, Long tenantId);

    void deleteByFollower_UserIdAndFollowing_UserIdAndTenantId(
            Long followerId, Long followingId, Long tenantId);

    long countByFollowing_UserIdAndTenantId(Long userId, Long tenantId);

    long countByFollower_UserIdAndTenantId(Long userId, Long tenantId);

    boolean existsByFollower_UserIdAndFollowing_UserId(Long followerUserId, Long targetUserId);

    List<Follower> findByFollowing_UserId(Long userId);
}
