package it.live.brainbox.service.impl;

import it.live.brainbox.entity.Serial;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.repository.SerialRepository;
import it.live.brainbox.service.SerialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SerialServiceImpl implements SerialService {
    private final SerialRepository serialRepository;

    @Override
    public ResponseEntity<ApiResponse> addSerial(Serial serial) {
        serialRepository.save(serial);
        return ResponseEntity.ok(ApiResponse.builder().message("Serial saved").status(200).build());
    }

    @Override
    public ResponseEntity<ApiResponse> updateSerial(Long serialId, Serial serial) {
        Serial serialEdit = serialRepository.findById(serialId).orElseThrow(() -> new NotFoundException("No such serial exists"));
        serialEdit.setName(serial.getName());
        serialRepository.save(serialEdit);
        return ResponseEntity.ok(ApiResponse.builder().message("Serial updated").status(200).build());
    }

    @Override
    public ResponseEntity<ApiResponse> deleteSerial(Long serialId) {
        try {
            serialRepository.deleteById(serialId);
            return ResponseEntity.ok(ApiResponse.builder().message("Serial deleted with Movies").status(200).build());
        } catch (Exception e) {
            throw new MainException("Error during process");
        }
    }

    @Override
    public Serial getSerial(Long serialId) {
        return serialRepository.findById(serialId).orElseThrow(() -> new NotFoundException("No such serial exists"));
    }

    @Override
    public Page<?> getSerialPage(int page, int size) {
        return serialRepository.findAll(PageRequest.of(page, size));
    }
}
