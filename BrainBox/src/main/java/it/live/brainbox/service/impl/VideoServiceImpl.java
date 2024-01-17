package it.live.brainbox.service.impl;

import it.live.brainbox.entity.Video;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.repository.VideoRepository;
import it.live.brainbox.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.PageRequest.of;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;

    @Override
    public ResponseEntity<ApiResponse> addVideo(Video video) {
        videoRepository.save(video);
        return ResponseEntity.ok(ApiResponse.builder().message("Saved").status(200).build());
    }

    @Override
    public Page<Video> getVideo(int page, int size) {
        return videoRepository.findAll(of(page, size, Sort.by("createdAt").descending()));
    }
}
