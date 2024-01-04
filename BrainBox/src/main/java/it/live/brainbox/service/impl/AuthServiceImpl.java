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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private Boolean isDebug = false;

    @Override
    public ResponseEntity<ApiResponse> regLog(UserDTO userDTO) {
        if (userRepository.findByEmailAndUniqueId(userDTO.getEmail(), userDTO.getUniqueId()).isPresent())
            return ResponseEntity.ok(ApiResponse.builder().message("Welcome").status(200).object(jwtProvider.generateToken(userDTO.getEmail())).build());
        userRepository.save(userMapper.toEntity(userDTO));
        return ResponseEntity.ok(ApiResponse.builder().message("Welcome").status(200).object(jwtProvider.generateToken(userDTO.getEmail())).build());
    }

    @Override
    public ResponseEntity<ApiResponse> isDebug() {
        if (isDebug)
            return ResponseEntity.ok(ApiResponse.builder().message("Xush kelibsiz").status(200).object(jwtProvider.generateToken("dev@gmail.com")).build());
        return null;
    }

    @Override
    public Boolean onOrOf() {
        if (isDebug) {
            isDebug = false;
            return false;
        } else {
            isDebug = true;
            return true;
        }
    }


}
