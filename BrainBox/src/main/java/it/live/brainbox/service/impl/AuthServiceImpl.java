package it.live.brainbox.service.impl;

import it.live.brainbox.entity.enums.SystemRoleName;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.jwt.JwtProvider;
import it.live.brainbox.mapper.UserMapper;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.UserDTO;
import it.live.brainbox.repository.UserRepository;
import it.live.brainbox.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<ApiResponse> regLog(UserDTO userDTO) {
        if (userRepository.findByEmailAndUniqueId(userDTO.getEmail(), userDTO.getUniqueId()).isPresent())
            return ResponseEntity.ok(ApiResponse.builder().message("Welcome").status(200).object(jwtProvider.generateToken(userDTO.getEmail())).build());
        userRepository.save(userMapper.toEntity(userDTO));
        return ResponseEntity.ok(ApiResponse.builder().message("Welcome").status(200).object(jwtProvider.generateToken(userDTO.getEmail())).build());
    }

    @Override
    public ResponseEntity<ApiResponse> telegramAdminAuth(String login, String password) {
        if (!userRepository.existsByEmailAndUniqueIdAndSystemRoleName(login, password , SystemRoleName.ROLE_ADMIN))
            throw new MainException("Login yoki parolingiz xato");

        return ResponseEntity.ok(ApiResponse.builder().message("Xush kelibsiz").status(200).object(jwtProvider.generateToken(login)).build());
    }


}
