package it.live.brainbox.service.impl;

import it.live.brainbox.entity.Language;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.mapper.LanguageMapper;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.LanguageDTO;
import it.live.brainbox.repository.LanguageRepository;
import it.live.brainbox.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Override
    public ResponseEntity<ApiResponse> addLanguage(LanguageDTO languageDTO) {
        languageRepository.save(languageMapper.toEntity(languageDTO));
        return ResponseEntity.ok(ApiResponse.builder().message("Language saved").status(200).build());
    }

    @Override
    public ResponseEntity<ApiResponse> deleteLanguage(Long languageId) {
        try {
            languageRepository.deleteById(languageId);
            return ResponseEntity.ok(ApiResponse.builder().message("Language deleted").status(200).build());
        } catch (Exception e) {
            throw new MainException("Error deleting language");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updatelanguage(Long languageId, LanguageDTO languageDTO) {
        Language editedLanguage = languageRepository.findById(languageId).orElseThrow(() -> new NotFoundException("No such language found"));
        editedLanguage.setName(languageDTO.getName());
        languageRepository.save(editedLanguage);
        return ResponseEntity.ok(ApiResponse.builder().message("Language upadated").status(200).build());
    }

    @Override
    public List<Language> getLanguageAllOr(Long languageId) {
        if (languageId != null)
            return languageRepository.findById(languageId).stream().toList();
        return languageRepository.findAll().stream().toList();
    }
}
