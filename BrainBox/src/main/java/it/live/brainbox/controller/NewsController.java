package it.live.brainbox.controller;

import it.live.brainbox.entity.News;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addNews")
    public ResponseEntity<ApiResponse> add(@RequestParam String title, @RequestParam String dialog, @RequestParam MultipartFile multipartFile) {
        return newsService.add(title, dialog, multipartFile);
    }

    @GetMapping("/getNews")
    public Page<News> get(@RequestParam int page, @RequestParam int size) {
        return newsService.get(page, size);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> update(@RequestParam String title, @RequestParam String dialog, @PathVariable Long id, @RequestParam MultipartFile multipartFile) {
        return newsService.update(title, dialog, id, multipartFile);
    }

    @PostMapping("/seen/{newsId}")
    public ResponseEntity<ApiResponse> addSeen(@PathVariable Long newsId) {
        return newsService.addSeen(newsId);
    }

    @GetMapping("/getLastNews")
    public News getOne() {
        return newsService.getLastNews();
    }

    @GetMapping("/image")
    public ResponseEntity<?> viewOneFile(@RequestParam String path) throws MalformedURLException {
        return newsService.viewOneFile(path);
    }
}
