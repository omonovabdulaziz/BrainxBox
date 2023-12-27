package it.live.brainbox.service;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    ResponseEntity<ApiResponse> regLog(UserDTO registerDTO);

    ResponseEntity<ApiResponse> telegramAdminAuth(String login, String password);
}
