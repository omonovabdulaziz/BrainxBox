package it.live.brainbox.service;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.SavedWordDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface SavedWordService {
    ResponseEntity<ApiResponse> add(String word , String translateWord);

    ResponseEntity<ApiResponse> delete(UUID id);

    List<SavedWordDto> get();
}
