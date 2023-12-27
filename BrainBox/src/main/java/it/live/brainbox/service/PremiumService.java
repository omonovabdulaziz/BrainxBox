package it.live.brainbox.service;

import it.live.brainbox.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface PremiumService {
    ResponseEntity<ApiResponse> setPremium(Long userId);

}
