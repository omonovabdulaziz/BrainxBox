package it.live.brainbox.service;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.PageSender;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SubtitleService {

    ResponseEntity<ApiResponse> deleteSubtitle(Long movieId , Long  languageId);

    PageSender<?> getWordsByCount(long languageId , int count, long movieId , int page , int size);

    ResponseEntity<ApiResponse> addSubtitle(Long movieId, MultipartFile file , Long languageId);

    ResponseEntity<ApiResponse> updateSubtitle(Long movieId, Long languageId, MultipartFile file);

}
