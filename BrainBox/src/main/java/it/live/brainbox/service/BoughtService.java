package it.live.brainbox.service;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.BoughtMovieDTO;
import org.springframework.http.ResponseEntity;

public interface BoughtService {
    ResponseEntity<ApiResponse> buy(BoughtMovieDTO boughtMovieDTO);

}
