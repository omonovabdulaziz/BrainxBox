package it.live.brainbox.controller;

import it.live.brainbox.entity.Video;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addVideo")
    public ResponseEntity<ApiResponse> addVideo(@Valid @RequestBody Video video) {
        return videoService.addVideo(video);
    }


    @GetMapping("/getVideoPage")
    public Page<Video> getVideo(@RequestParam int page, @RequestParam int size) {
        return videoService.getVideo(page, size);
    }
}
