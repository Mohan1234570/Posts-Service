package in.krish.controller;

import in.krish.binding.ApiResponse;
import in.krish.binding.UserDTO;
import in.krish.binding.UserProfileResponse;
import in.krish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ================= SEARCH USERS =================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> searchUsers(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserDTO> users = userService.searchUsers(query, page, size);

        ApiResponse<Page<UserDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Users fetched successfully",
                users
        );

        return ResponseEntity.ok(response);
    }

    // ================= USER PROFILE =================
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @PathVariable Long userId,
            @RequestHeader("X-Tenant-Id") Long tenantId
    ) {
        UserProfileResponse profile = userService.getProfile(userId, tenantId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "User profile fetched",
                        profile
                )
        );
    }
}
