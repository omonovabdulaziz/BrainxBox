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
    private final LanguageRepository languageRepository;

    public SubtitleWord toEntity(SubtitleWordPyDTO subtitleWordPyDTO, WordDefinitionApiDTO wordDefinitionApiDTO, Pronunciation pronunciation, Long movieId, Long languageId) {
        return SubtitleWord.builder()
                .count(subtitleWordPyDTO.getCount())
                .value(subtitleWordPyDTO.getWord())
                .secondLanguageValue(subtitleWordPyDTO.getTranslation())
                .definition(String.valueOf(wordDefinitionApiDTO != null ? wordDefinitionApiDTO.getDefinitions() : null))
                .pronunciation(String.valueOf(pronunciation != null ? pronunciation.getPronunciation() : null))
                .movie(movieRepository.findById(movieId).orElseThrow(() -> new NotFoundException("No such movie exists")))
                .language(languageRepository.findById(languageId).orElseThrow(() -> new NotFoundException("No such language exists")))
                .build();
    }

    public SubtitleWordDTO toDTO(SubtitleWord subtitleWord) {
        return SubtitleWordDTO.builder()
                .count(subtitleWord.getCount())
                .movieId(subtitleWord.getMovie().getId())
                .value(subtitleWord.getValue())
                .secondLanguageValue(subtitleWord.getSecondLanguageValue())
                .definition(subtitleWord.getDefinition())
                .pronunciation(subtitleWord.getPronunciation())
                .languageId(subtitleWord.getLanguage().getId())
                .build();
    }
}
