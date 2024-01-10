package it.live.brainbox.service;

import it.live.brainbox.entity.Video;
import it.live.brainbox.payload.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface VideoService {
    ResponseEntity<ApiResponse> addVideo(Video video);

    Page<Video> getVideo(int page, int size);
}
