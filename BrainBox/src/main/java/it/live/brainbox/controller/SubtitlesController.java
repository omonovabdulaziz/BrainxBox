package it.live.brainbox.controller;


import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.PageSender;
import it.live.brainbox.service.SubtitleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/subtitleWords")
@RestController
@RequiredArgsConstructor
public class SubtitlesController {
    private final SubtitleService subtitleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addSubtitle/{movieId}")
    public ResponseEntity<ApiResponse> addSubtitle(@PathVariable Long movieId, @RequestParam MultipartFile file, @RequestParam Long languageId) {
        return subtitleService.addSubtitle(movieId, file, languageId);
    }


    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateSubtitle/{movieId}")
    public ResponseEntity<ApiResponse> updateSubtitle(@PathVariable Long movieId, @RequestParam Long languageId, @RequestParam MultipartFile file) {
        return subtitleService.updateSubtitle(movieId, languageId, file);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteSubtitle/{movieId}")
    public ResponseEntity<ApiResponse> deleteSubtitle(@PathVariable Long movieId, @RequestParam Long languageId) {
        return subtitleService.deleteSubtitle(movieId, languageId);
    }


    @GetMapping("/getWordsByCount")
    public PageSender<?> getWordsByCount(@RequestParam(value = "language") long languageId, @RequestParam(value = "count", required = false) int count, @RequestParam("movieId") long movieId, @RequestParam int page, @RequestParam int size) {
        return subtitleService.getWordsByCount(languageId, count, movieId, page, size);
    }

}
