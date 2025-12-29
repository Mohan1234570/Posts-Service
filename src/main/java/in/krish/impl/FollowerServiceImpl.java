//package in.krish.impl;
//
//import in.krish.entity.Follower;
//import in.krish.entity.User;
//import in.krish.repo.FollowerRepo;
//import in.krish.repo.UserRepo;
//import in.krish.service.FollowerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class FollowerServiceImpl implements FollowerService {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private FollowerRepo followerRepo;
//    @Transactional
//    public boolean followByEmail(String followerEmail, Long followingId) {
//        User follower = userRepo.findByEmailid(followerEmail);
//        if (follower == null) {
//            throw new RuntimeException("Follower not found");
//        }
//
//        if (follower.getUserId().equals(followingId)) {
//            throw new RuntimeException("You cannot follow yourself");
//        }
//
//        if (followerRepo.existsByFollower_UserIdAndFollowing_UserId(follower.getUserId(), followingId)) {
//            return false; // already following
//        }
//
//        User following = userRepo.findById(followingId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Follower f = new Follower();
//        f.setFollower(follower);
//        f.setFollowing(following);
//
//        followerRepo.save(f);
//        return true;
//    }
//
//    @Transactional
//    public boolean unfollowByEmail(String followerEmail, Long followingId) {
//        User follower = userRepo.findByEmailid(followerEmail);
//        if (follower == null) {
//            throw new RuntimeException("Follower not found");
//        }
//
//        followerRepo.deleteByFollower_UserIdAndFollowing_UserId(follower.getUserId(), followingId);
//        return true;
//    }
//
//    public boolean checkFollowStatusByEmail(String followerEmail, Long targetId) {
//        User follower = userRepo.findByEmailid(followerEmail);
//        if (follower == null) {
//            throw new RuntimeException("Follower not found");
//        }
//
//        return followerRepo.existsByFollower_UserIdAndFollowing_UserId(follower.getUserId(), targetId);
//    }
//
//    public long countFollowers(Long userId) {
//        return followerRepo.countFollowers(userId);
//    }
//
//    public long countFollowing(Long userId) {
//        return followerRepo.countFollowing(userId);
//    }
//
//}


package in.krish.impl;

import in.krish.entity.Follower;
import in.krish.entity.User;
import in.krish.repo.FollowerRepo;
import in.krish.repo.UserRepo;
import in.krish.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FollowerRepo followerRepo;

    @Override
    public boolean follow(
            Long followerUserId,
            Long targetUserId,
            Long tenantId
    ) {
        if (followerUserId.equals(targetUserId)) {
            throw new RuntimeException("You cannot follow yourself");
        }

        User follower = userRepo.findByUserIdAndTenantId(followerUserId, tenantId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        User following = userRepo.findByUserIdAndTenantId(targetUserId, tenantId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if (followerRepo.existsByFollower_UserIdAndFollowing_UserId(
                followerUserId, targetUserId)) {
            return false;
        }

        Follower f = new Follower();
        f.setFollower(follower);
        f.setFollowing(following);
        f.setTenantId(tenantId);

        followerRepo.save(f);
        return true;
    }

    @Override
    public boolean unfollow(
            Long followerUserId,
            Long targetUserId,
            Long tenantId
    ) {
        followerRepo.deleteByFollower_UserIdAndFollowing_UserIdAndTenantId(
                followerUserId, targetUserId, tenantId);
        return true;
    }

    @Override
    public boolean isFollowing(
            Long followerUserId,
            Long targetUserId,
            Long tenantId
    ) {
        return followerRepo.existsByFollower_UserIdAndFollowing_UserIdAndTenantId(
                followerUserId, targetUserId, tenantId);
    }

    @Override
    public long countFollowers(Long userId, Long tenantId) {
        return followerRepo.countByFollowing_UserIdAndTenantId(userId, tenantId);
    }

    @Override
    public long countFollowing(Long userId, Long tenantId) {
        return followerRepo.countByFollower_UserIdAndTenantId(userId, tenantId);
    }
}
