package it.live.brainbox.controller;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.service.BoughtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bought-movie")
@RequiredArgsConstructor
public class BoughtMovieController {
    private final BoughtService boughtService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @PostMapping("/buy")
    public ResponseEntity<ApiResponse> buy(@RequestParam Long movieId) {
        return boughtService.buy(movieId);
    }
}
