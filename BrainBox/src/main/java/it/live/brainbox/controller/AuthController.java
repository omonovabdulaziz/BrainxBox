package it.live.brainbox.controller;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.UserDTO;
import it.live.brainbox.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/regLog")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserDTO registerDTO) {
        return authService.regLog(registerDTO);
    }

    @PostMapping("/isDebug")
    public ResponseEntity<ApiResponse> isDebug() {
        return authService.isDebug();
    }

    @PostMapping("/onOrOfDebug")
    public Boolean onOrOf() {
        return authService.onOrOf();
    }

}
