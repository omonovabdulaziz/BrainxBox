package it.live.brainbox.service.impl;

import it.live.brainbox.config.SecurityConfiguration;
import it.live.brainbox.entity.User;
import it.live.brainbox.entity.enums.SystemRoleName;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.UserDTO;
import it.live.brainbox.repository.UserRepository;
import it.live.brainbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("User not found"));
    }


    @Override
    public ResponseEntity<ApiResponse> updateUser(Long userId, UserDTO userDTO, Integer addCoin) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (userDTO != null) {
            user.setName(userDTO.getName());
            user.setSurname(userDTO.getSurname());
            user.setUniqueId(userDTO.getUniqueId());
            user.setImageUrl(userDTO.getImageUrl());
        }
        user.setCoins(user.getCoins() + addCoin);
        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.builder().message("success").status(200).build());
    }

    @Override
    public User getUserById(Long userId) {
        if (userId != null) {
            return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        }
        return SecurityConfiguration.getOwnSecurityInformation();
    }

    @Override
    public Page<?> getAllUserByPage(int page, int size) {
        return userRepository.findAllBySystemRoleName(SystemRoleName.ROLE_USER, PageRequest.of(page, size));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteMyAccount() {
        try {
            userRepository.delete(SecurityConfiguration.getOwnSecurityInformation());
        } catch (Exception e) {
            throw new MainException("Error");
        }
        return ResponseEntity.ok(ApiResponse.builder().message("Deleted").status(200).build());
    }
}
