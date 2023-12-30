package it.live.brainbox.controller;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.SavedWordDto;
import it.live.brainbox.service.SavedWordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/saved-word")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
public class SavedWordController {
    private final SavedWordService savedWordService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@RequestParam String word , @RequestParam String translateWord) {
        return savedWordService.add(word , translateWord);
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestParam UUID id) {
        return savedWordService.delete(id);
    }

    @GetMapping("/getSavedWords")
    public List<SavedWordDto> get() {
        return savedWordService.get();
    }

}
