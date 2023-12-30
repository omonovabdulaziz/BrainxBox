package it.live.brainbox.service.impl;

import it.live.brainbox.config.SecurityConfiguration;
import it.live.brainbox.entity.SavedWord;
import it.live.brainbox.mapper.SaveWordMapper;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.SavedWordDto;
import it.live.brainbox.repository.SavedWordRepository;
import it.live.brainbox.service.SavedWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SavedWordServiceImpl implements SavedWordService {
    private final SavedWordRepository savedWordRepository;
    private final SaveWordMapper saveWordMapper;

    @Override
    public ResponseEntity<ApiResponse> add(String word , String translateWord) {
        savedWordRepository.save(SavedWord.builder().translateWord(translateWord).value(word).user(SecurityConfiguration.getOwnSecurityInformation()).build());
        return ResponseEntity.ok(ApiResponse.builder().status(201).message("Success").build());
    }

    @Override
    public ResponseEntity<ApiResponse> delete(UUID id) {
        savedWordRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.builder().message("deleted").status(200).build());
    }

    @Override
    public List<SavedWordDto> get() {
        return savedWordRepository.findAllByUserId(SecurityConfiguration.getOwnSecurityInformation().getId()).stream().map(saveWordMapper::toDTO).collect(Collectors.toList());
    }
}
