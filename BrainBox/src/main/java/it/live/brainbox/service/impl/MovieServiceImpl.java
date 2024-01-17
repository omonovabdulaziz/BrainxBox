package it.live.brainbox.service.impl;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import it.live.brainbox.config.SecurityConfiguration;
import it.live.brainbox.entity.Movie;
import it.live.brainbox.entity.User;
import it.live.brainbox.entity.enums.Level;
import it.live.brainbox.entity.enums.SystemRoleName;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.mapper.MovieMapper;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.MovieDTO;
import it.live.brainbox.repository.BoughtMovieRepository;
import it.live.brainbox.repository.MovieRepository;
import it.live.brainbox.repository.SerialRepository;
import it.live.brainbox.service.MovieService;
import it.live.brainbox.utils.BrainBoxBot;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.PageRequest.of;


@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieMapper movieMapper;
    private final MovieRepository movieRepository;
    private final SerialRepository serialRepository;
    private final BoughtMovieRepository boughtMovieRepository;
    private final BrainBoxBot bot;
    private static final String apiKey = "4daf298e";

    @Override
    public ResponseEntity<ApiResponse> addMovie(MovieDTO movieDTO) {
        if (movieRepository.existsByNameIgnoreCase(movieDTO.getName()))
            throw new MainException("Bunday nomli kino mavjud");
        Movie movie = movieMapper.toEntity(movieDTO);
        List<String> imageOfMovie = getImageOfMovie(movieDTO.getName());
        assert imageOfMovie != null;
        movie.setAvatarUrl(!Objects.equals(imageOfMovie.get(0), "null") ? imageOfMovie.get(0) : null);
        movie.setGenre(!Objects.equals(imageOfMovie.get(1), "null") ? imageOfMovie.get(1).split(", ")[0] : "ADVENTURE");
        movie.setDescription(!Objects.equals(imageOfMovie.get(2), "null") ? imageOfMovie.get(2) : "Movie is Very Good. This Movie .....");
        movie.setPrice(35);
        Movie save = movieRepository.save(movie);
        return ResponseEntity.ok(ApiResponse.builder().status(200).message("Movie saved").object(save.getId()).build());
    }

    @Override
    public ResponseEntity<ApiResponse> updateMovie(MovieDTO movieDTO, Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new NotFoundException("No such movie exists"));
        if (movieDTO.getUpdateImageUrl() != null || movieDTO.getUpdateImageGenre() != null) {
            movie.setName(movieDTO.getName());
            movie.setAvatarUrl(movieDTO.getUpdateImageUrl());
            movie.setGenre(movieDTO.getUpdateImageGenre());
            movie.setDescription(movieDTO.getDescription());
        } else {
            if (!Objects.equals(movie.getName(), movieDTO.getName())) {
                movie.setName(movieDTO.getName());
                List<String> imageOfMovie = getImageOfMovie(movieDTO.getName());
                assert imageOfMovie != null;
                movie.setAvatarUrl(!Objects.equals(imageOfMovie.get(0), "null") ? imageOfMovie.get(0) : null);
                movie.setGenre(!Objects.equals(imageOfMovie.get(1), "null") ? imageOfMovie.get(1).split(", ")[0] : null);
                movie.setDescription(!Objects.equals(imageOfMovie.get(2), "null") ? imageOfMovie.get(2) : "Movie is Very Good. This Movie .....");
            }
        }

        movie.setLevel(movieDTO.getLevel());
        movie.setPrice(35);
        if (movieDTO.getSerialId() != null)
            movie.setSerial(serialRepository.findById(movieDTO.getSerialId()).orElseThrow(() -> new NotFoundException("No such serial exists")));
        movie.setBelongAge(movieDTO.getBelongAge());
        movieRepository.save(movie);
        return ResponseEntity.ok(ApiResponse.builder().message("Movie updated").status(200).build());
    }


    @Override
    public Page<?> getAllMoviePage(int page, int size, Level level, String genre, boolean noSubtitle) {
        User user = SecurityConfiguration.getOwnSecurityInformation();
        Page<Movie> movies;
        if (level != null) {
            movies = movieRepository.findAllByLevel(level, of(page, size, Sort.by("createdAt").descending()));
        } else if (genre != null) {
            movies = movieRepository.findAllByGenre(genre, of(page, size));
        } else if (noSubtitle) {
            movies = movieRepository.findAll(of(page, size, Sort.by("createdAt").descending()));
        } else {
            movies = movieRepository.findAll(of(page, size, Sort.by("createdAt").descending()));
        }
        for (Movie movie : movies) {
            if (boughtMovieRepository.existsByUserIdAndMovieId(user.getId(), movie.getId())) {
                movie.setIsBought(true);
            } else {
                movie.setIsBought(false);
            }
        }
        return movies;
    }

    @Override
    public Movie getMovie(Long movieId) {
        User user = SecurityConfiguration.getOwnSecurityInformation();
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new NotFoundException("No such movie exists"));
        if (user.getSystemRoleName() == SystemRoleName.ROLE_USER) {
            if (boughtMovieRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
                movie.setIsBought(true);
            } else {
                movie.setIsBought(false);
            }
            return movie;
        }
        return movie;
    }


    @Override
    public List<Movie> getAllMovieBySerialId(Long serialId) {
        User user = SecurityConfiguration.getOwnSecurityInformation();
        List<Movie> movies = new ArrayList<>();
        if (user.getSystemRoleName() == SystemRoleName.ROLE_USER) {
            for (Movie movie : movieRepository.findAllBySerialId(serialId)) {
                if (boughtMovieRepository.existsByUserIdAndMovieId(user.getId(), movie.getId())) {
                    movie.setIsBought(true);
                } else {
                    movie.setIsBought(false);
                }
                movies.add(movie);
            }
            return movies;
        }
        return movieRepository.findAllBySerialId(serialId).stream().toList();
    }


    @Override
    public ResponseEntity<ApiResponse> deleteMovie(Long movie) {
        try {
            movieRepository.findById(movie).orElseThrow();
            movieRepository.deleteById(movie);
            return ResponseEntity.ok(ApiResponse.builder().message("Deleted").status(200).build());
        } catch (Exception e) {
            throw new MainException("Failed to delete");
        }
    }

    @Override
    public List<Movie> searchMovie(String keyWord) {
        User user = SecurityConfiguration.getOwnSecurityInformation();
        List<Movie> movies = new ArrayList<>();
        List<Movie> pageMovie = movieRepository.findAllByNameLikeIgnoreCase("%" + keyWord + "%");
        for (Movie movie : pageMovie) {
            movie.setIsBought(boughtMovieRepository.existsByUserIdAndMovieId(user.getId(), movie.getId()));
            movies.add(movie);
        }
        if (movies.isEmpty()) {
            throw new NotFoundException("There is no movie with this name If you want it, please share the name of the movie with us");
        }
        return movies;
    }

    @Override
    public ResponseEntity<ApiResponse> addRequest(String request) {
        try {
            bot.sendToChannel(SecurityConfiguration.getOwnSecurityInformation().getName(), request);
        } catch (Exception e) {
            throw new MainException("Xatalik");
        }
        return ResponseEntity.ok(ApiResponse.builder().status(200).message("Ok").build());
    }


    public static List<String> getImageOfMovie(String movieName) {
        try {
            Request request = new Request.Builder()
                    .url("http://www.omdbapi.com/?apikey=" + apiKey + "&t=" + movieName)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();

            if (response.code() == 200) { // HTTP_OK
                String jsonResponse = response.body().string();
                return parseAndPrintMovieDetails(jsonResponse);
            } else {
                System.out.println("Image download error. HTTP Status Code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private static List<String> parseAndPrintMovieDetails(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            List<String> strings = new ArrayList<>();
            strings.add(jsonObject.optString("Poster", "null"));
            strings.add(jsonObject.optString("Genre", "null"));
            strings.add(jsonObject.optString("Plot", "null"));
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
