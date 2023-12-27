package it.live.brainbox.mapper;

import it.live.brainbox.entity.Movie;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.payload.MovieDTO;
import it.live.brainbox.repository.LanguageRepository;
import it.live.brainbox.repository.SerialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieMapper {
    private final SerialRepository serialRepository;

    public Movie toEntity(MovieDTO movieDTO) {
        return Movie
                .builder()
                .belongAge(movieDTO.getBelongAge())
                .price(movieDTO.getPrice())
                .name(movieDTO.getName())
                .description(movieDTO.getDescription())
                .level(movieDTO.getLevel())
                 .serial(movieDTO.getSerialId() == null ? null : serialRepository.findById(movieDTO.getSerialId()).get())
                .build();
    }


}
