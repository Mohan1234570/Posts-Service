package in.krish.service;

import in.krish.entity.User;

public interface FollowerService {
    public boolean follow(
            Long followerUserId,
            Long targetUserId,
            Long tenantId);
    public boolean unfollow(
            Long followerUserId,
            Long targetUserId,
            Long tenantId
    );
    public boolean isFollowing(
            Long followerUserId,
            Long targetUserId,
            Long tenantId
    );
    public long countFollowers(Long userId, Long tenantId);
    public long countFollowing(Long userId, Long tenantId);
}

