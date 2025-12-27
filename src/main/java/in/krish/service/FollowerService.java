package in.krish.service;

import in.krish.entity.User;

public interface FollowerService {
    public boolean followByEmail(String followerEmail, Long followingId);
    public boolean unfollowByEmail(String followerEmail, Long followingId);
    public boolean checkFollowStatusByEmail(String followerEmail, Long targetId);
    public long countFollowers(Long userId);
    public long countFollowing(Long userId);
}

