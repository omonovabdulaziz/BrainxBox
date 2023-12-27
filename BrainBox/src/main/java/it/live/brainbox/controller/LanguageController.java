package it.live.brainbox.controller;

import it.live.brainbox.entity.Language;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.LanguageDTO;
import it.live.brainbox.service.LanguageService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/language")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addLanguage(@Valid @RequestBody LanguageDTO languageDTO) {
        return languageService.addLanguage(languageDTO);
    }


@Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{languageId}")
    public ResponseEntity<ApiResponse> deleteLanguage(@PathVariable Long languageId) {
        return languageService.deleteLanguage(languageId);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{languageId}")
    public ResponseEntity<ApiResponse> updateLanguage(@PathVariable Long languageId, @Valid @RequestBody LanguageDTO languageDTO) {
        return languageService.updatelanguage(languageId, languageDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    @GetMapping("/getLanguage")
    public List<Language> getLanguageAllOr(@RequestParam(value = "languageId", required = false) Long languageId) {
        return languageService.getLanguageAllOr(languageId);
    }
}
