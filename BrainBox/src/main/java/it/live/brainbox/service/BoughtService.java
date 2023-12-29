package it.live.brainbox.service;

import it.live.brainbox.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface BoughtService {
    ResponseEntity<ApiResponse> buy( Long movieId);

}
