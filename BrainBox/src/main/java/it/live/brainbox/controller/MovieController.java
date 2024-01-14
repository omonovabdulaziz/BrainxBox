package it.live.brainbox.controller;


import it.live.brainbox.entity.Movie;
import it.live.brainbox.entity.enums.Level;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.MovieDTO;
import it.live.brainbox.service.MovieService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/getAllMoviePage")
    public Page<?> getAllMoviePage(@RequestParam int page, @RequestParam int size, @RequestParam(value = "level", required = false) Level level, @RequestParam(value = "genre", required = false) String genre, @RequestParam(required = false) boolean noSubtitle) {
        return movieService.getAllMoviePage(page, size, level, genre, noSubtitle);
    }

    @GetMapping("/getMovie/{movieId}")
    public Movie getMovie(@PathVariable Long movieId) {
        return movieService.getMovie(movieId);
    }


    @GetMapping("/getMovieBySerialId/{serialId}")
    public List<Movie> getMovieBySerialId(@PathVariable Long serialId) {
        return movieService.getAllMovieBySerialId(serialId);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addMovie")
    public ResponseEntity<ApiResponse> addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateMovie/{movieId}")
    public ResponseEntity<ApiResponse> updateMovie(@Valid @RequestBody MovieDTO movieDTO, @PathVariable Long movieId) {
        return movieService.updateMovie(movieDTO, movieId);
    }


    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteMovie/{movie}")
    public ResponseEntity<ApiResponse> deleteMovie(@PathVariable Long movie) {
        return movieService.deleteMovie(movie);
    }

    @GetMapping("/search-movie")
    public List<Movie> searchMovie(@RequestParam String keyWord) {
        return movieService.searchMovie(keyWord);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/addRequestMovie")
    public ResponseEntity<ApiResponse> addRequestMovie(@RequestParam String request) {
        return movieService.addRequest(request);
    }
}