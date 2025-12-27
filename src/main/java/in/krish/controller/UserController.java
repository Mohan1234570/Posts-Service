package in.krish.controller;

import in.krish.binding.ApiResponse;
import in.krish.binding.UserDTO;
import in.krish.entity.User;
import in.krish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> searchUsers(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDTO> users = userService.searchUsers(query, page, size);

        ApiResponse<Page<UserDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Users fetched successfully",
                users
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }


}
