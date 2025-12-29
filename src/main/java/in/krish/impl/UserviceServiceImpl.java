package in.krish.impl;

import in.krish.PostResponse;
import in.krish.binding.UserDTO;
import in.krish.binding.UserProfileResponse;
import in.krish.entity.Post;
import in.krish.entity.User;
import in.krish.repo.PostRepo;
import in.krish.repo.UserRepo;
import in.krish.service.FollowerService;
import in.krish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserviceServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private PostRepo postRepo;

    // ================= SEARCH USERS =================
    @Override
    @org.springframework.cache.annotation.Cacheable(
            value = "userSearchCache",
            key = "#query + '_' + #page + '_' + #size"
    )
    public Page<UserDTO> searchUsers(String query, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return Page.empty(pageable);
        }

        return userRepo.searchUsers(query, pageable)
                .map(this::convertToDTO);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmailid(),
                user.getProfileImageUrl()
        );
    }

    // ================= USER PROFILE =================
    @Override
    public UserProfileResponse getProfile(Long userId, Long tenantId) {

        User user = userRepo.findByUserIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long followers = followerService.countFollowers(userId, tenantId);
        long following = followerService.countFollowing(userId, tenantId);

        List<PostResponse> posts = postRepo
                .findByUserIdAndTenantId(userId, tenantId)
                .stream()
                .map(p -> new PostResponse(
                        p.getId(),          // ✅ correct
                        p.getContent(),
                        p.getImageUrl(),
                        p.getCreatedAt()
                ))
                .toList();

        return new UserProfileResponse(
                user.getUserId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmailid(),
                followers,
                following,
                posts
        );

    }
}
