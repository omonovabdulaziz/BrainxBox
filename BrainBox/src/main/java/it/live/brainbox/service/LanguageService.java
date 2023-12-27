package it.live.brainbox.service;

import it.live.brainbox.entity.Language;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.LanguageDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LanguageService {
    ResponseEntity<ApiResponse> addLanguage(LanguageDTO languageDTO);

    ResponseEntity<ApiResponse> deleteLanguage(Long languageId);

    ResponseEntity<ApiResponse> updatelanguage(Long languageId, LanguageDTO languageDTO);

    List<Language> getLanguageAllOr(Long languageId);
}
