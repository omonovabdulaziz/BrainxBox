package it.live.brainbox.service.impl;

import it.live.brainbox.entity.BoughtMovie;
import it.live.brainbox.entity.Movie;
import it.live.brainbox.entity.User;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.BoughtMovieDTO;
import it.live.brainbox.repository.BoughtMovieRepository;
import it.live.brainbox.repository.MovieRepository;
import it.live.brainbox.repository.UserRepository;
import it.live.brainbox.service.BoughtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BoughtServiceImpl implements BoughtService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final BoughtMovieRepository boughtMovieRepository;

    @Override
    public ResponseEntity<ApiResponse> buy(BoughtMovieDTO boughtMovieDTO) {
        User user = userRepository.findById(boughtMovieDTO.getUserId()).orElseThrow(() -> new NotFoundException("No such users exists"));
        Movie movie = movieRepository.findById(boughtMovieDTO.getMovieId()).orElseThrow(() -> new NotFoundException("No such movie exists"));
        if (boughtMovieRepository.existsByUserIdAndMovieId(user.getId() , movie.getId()))
            throw new MainException("You already unlocked this movie");
        user.setCoins(user.getCoins() - movie.getPrice());
        userRepository.save(user);
        boughtMovieRepository.save(BoughtMovie.builder().movie(movie).user(user).build());
        return ResponseEntity.ok(ApiResponse.builder().message("This movie unlocked for you").status(200).build());
    }
}
