package it.live.brainbox.controller;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.BoughtMovieDTO;
import it.live.brainbox.service.BoughtService;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bought-movie")
@RequiredArgsConstructor
public class BoughtMovieController {
    private final BoughtService boughtService;

    @Transactional
    @PostMapping("/buy")
    public ResponseEntity<ApiResponse> buy(@RequestBody BoughtMovieDTO boughtMovieDTO) {
        return boughtService.buy(boughtMovieDTO);
    }
}
