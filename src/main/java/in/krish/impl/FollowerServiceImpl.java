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
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FollowerRepo followerRepo;
    @Transactional
    public boolean followByEmail(String followerEmail, Long followingId) {
        User follower = userRepo.findByEmailid(followerEmail);
        if (follower == null) {
            throw new RuntimeException("Follower not found");
        }

        if (follower.getUserId().equals(followingId)) {
            throw new RuntimeException("You cannot follow yourself");
        }

        if (followerRepo.existsByFollower_UserIdAndFollowing_UserId(follower.getUserId(), followingId)) {
            return false; // already following
        }

        User following = userRepo.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Follower f = new Follower();
        f.setFollower(follower);
        f.setFollowing(following);

        followerRepo.save(f);
        return true;
    }

    @Transactional
    public boolean unfollowByEmail(String followerEmail, Long followingId) {
        User follower = userRepo.findByEmailid(followerEmail);
        if (follower == null) {
            throw new RuntimeException("Follower not found");
        }

        followerRepo.deleteByFollower_UserIdAndFollowing_UserId(follower.getUserId(), followingId);
        return true;
    }

    public boolean checkFollowStatusByEmail(String followerEmail, Long targetId) {
        User follower = userRepo.findByEmailid(followerEmail);
        if (follower == null) {
            throw new RuntimeException("Follower not found");
        }

        return followerRepo.existsByFollower_UserIdAndFollowing_UserId(follower.getUserId(), targetId);
    }

    public long countFollowers(Long userId) {
        return followerRepo.countFollowers(userId);
    }

    public long countFollowing(Long userId) {
        return followerRepo.countFollowing(userId);
    }

}
