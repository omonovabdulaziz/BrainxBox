package it.live.brainbox.service.impl;

import it.live.brainbox.exception.MainException;
import it.live.brainbox.mapper.SubtitleMapper;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.PageSender;
import it.live.brainbox.payload.SubtitleWordDTO;
import it.live.brainbox.repository.SubtitleRepository;
import it.live.brainbox.service.SubtitleService;
import it.live.brainbox.utils.WordDefinitionApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SubtitleServiceImpl implements SubtitleService {
    private final SubtitleRepository subtitleRepository;
    private final WordDefinitionApi wordDefinitionApi;
    private final SubtitleMapper subtitleMapper;

    @Override
    public ResponseEntity<ApiResponse> deleteSubtitle(Long movieId, Long languageId) {
        subtitleRepository.deleteAllByMovieIdAndLanguageId(movieId, languageId);
        return ResponseEntity.ok(ApiResponse.builder().message("All subtitles for this movie in this language have been removed").status(200).build());
    }

    @Override
    public PageSender<?> getWordsByCount(long languageId, int count, long movieId, int page, int size) {
        List<SubtitleWordDTO> list = subtitleRepository.getSubtitleWordsByAny(movieId, languageId, count, PageRequest.of(page, size)).stream().map(subtitleMapper::toDTO).toList();
        return new PageSender<>(subtitleRepository.getSubtitleWordsByAny(movieId, languageId, count).size(), list);
    }

    @Override
    public ResponseEntity<ApiResponse> addSubtitle(Long movieId, MultipartFile file, Long languageId) {
        if (wordDefinitionApi.addSubtitle(movieId, file, languageId))
            return ResponseEntity.ok(ApiResponse.builder().message("Added subtitles for this movie").status(200).build());

        throw new MainException("Error in process for add movie");
    }

    @Override
    public ResponseEntity<ApiResponse> updateSubtitle(Long movieId, Long languageId, MultipartFile file) {
        subtitleRepository.deleteAllByMovieIdAndLanguageId(movieId, languageId);
        if (wordDefinitionApi.addSubtitle(movieId, file, languageId))
            return ResponseEntity.ok(ApiResponse.builder().message("This movie subtitles updated").status(200).build());

        throw new MainException("Error in process");
    }
}


