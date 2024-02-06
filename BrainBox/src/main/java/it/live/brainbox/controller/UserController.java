package it.live.brainbox.controller;

import it.live.brainbox.config.SecurityConfiguration;
import it.live.brainbox.entity.User;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.UserDTO;
import it.live.brainbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    @PutMapping("/updateUser")
    public ResponseEntity<ApiResponse> updateUser(@RequestParam Long userId, @RequestBody(required = false) UserDTO userDTO, @RequestParam Integer coinCount) {
        return userService.updateUser(userId, userDTO, coinCount);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    @GetMapping("/getUserById")
    public User getUserById(@RequestParam(value = "userId", required = false) Long userId) {
        return userService.getUserById(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllUserByPage")
    public Page<?> getAllUserByPage(@RequestParam int page, @RequestParam int size) {
        return userService.getAllUserByPage(page, size);
    }

    @DeleteMapping("/deleteMyAccount")
    public ResponseEntity<ApiResponse> deleteMyAccount() {
        return userService.deleteMyAccount();
    }
}
