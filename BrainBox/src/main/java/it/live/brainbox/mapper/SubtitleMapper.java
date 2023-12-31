package it.live.brainbox.mapper;


import it.live.brainbox.entity.SubtitleWord;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.payload.Pronunciation;
import it.live.brainbox.payload.SubtitleWordDTO;
import it.live.brainbox.payload.SubtitleWordPyDTO;
import it.live.brainbox.payload.WordDefinitionApiDTO;
import it.live.brainbox.repository.LanguageRepository;
import it.live.brainbox.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubtitleMapper {
    private final MovieRepository movieRepository;

    public SubtitleWord toEntity(SubtitleWordPyDTO subtitleWordPyDTO, Pronunciation pronunciation, Long movieId) {
        return SubtitleWord.builder()
                .count(subtitleWordPyDTO.getCount())
                .value(subtitleWordPyDTO.getWord())
                .translation_en(subtitleWordPyDTO.getTranslation_en())
                .translation_ru(subtitleWordPyDTO.getTranslation_ru())
                .pronunciation(String.valueOf(pronunciation != null ? pronunciation.getPronunciation() : null))
                .movie(movieRepository.findById(movieId).orElseThrow(() -> new NotFoundException("No such movie exists")))
                .build();
    }

    public SubtitleWordDTO toDTO(SubtitleWord subtitleWord) {
        return SubtitleWordDTO.builder()
                .count(subtitleWord.getCount())
                .movieId(subtitleWord.getMovie().getId())
                .value(subtitleWord.getValue())
                .translation_ru(subtitleWord.getTranslation_ru())
                .translation_eng(subtitleWord.getTranslation_en())
                .pronunciation(subtitleWord.getPronunciation())
                .build();
    }
}
