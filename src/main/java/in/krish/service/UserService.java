package in.krish.service;

import in.krish.binding.UserDTO;
import in.krish.binding.UserProfileResponse;
import in.krish.entity.User;
import org.springframework.data.domain.Page;


public interface UserService {

	Page<UserDTO> searchUsers(String query, int page, int size);

	UserProfileResponse getProfile(Long userId, Long tenantId);
}
