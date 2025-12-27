
package in.krish.impl;

import in.krish.PostResponse;
import in.krish.binding.PostDTO;
import in.krish.binding.UserDTO;
import in.krish.binding.UserProfileResponse;
import in.krish.entity.Post;
import in.krish.entity.User;
import in.krish.repo.PostRepo;
import in.krish.repo.UserRepo;
import in.krish.service.FollowerService;
import in.krish.service.NotificationService;
import in.krish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Cacheable;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserviceServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private PostRepo postRepo;

    @Override
    @org.springframework.cache.annotation.Cacheable(value = "userSearchCache", key = "#query + '_' + #page + '_' + #size")
    public Page<UserDTO> searchUsers(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return Page.empty(pageable); // only return empty if query is blank
        }


        // Call repository method (must return Page<User>)
        Page<User> users = userRepo.searchUsers(query, pageable);

        // Map User to UserDTO
        return users.map(this::convertToDTO);
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
    public UserProfileResponse getProfile(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long followers = followerService.countFollowers(userId);
        long following = followerService.countFollowing(userId);

        List<PostResponse> posts = postRepo.findByUser_UserId(userId)
                .stream()
                .map(p -> new PostResponse(
                        p.getId(),
                        p.getContent(),      // must be TEXT not CLOB
                        p.getImageUrl(),     // must be BYTEA/String
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

