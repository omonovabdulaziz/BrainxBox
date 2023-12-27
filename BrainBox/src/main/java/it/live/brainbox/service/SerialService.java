package it.live.brainbox.service;

import it.live.brainbox.entity.Serial;
import it.live.brainbox.payload.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface SerialService {
    ResponseEntity<ApiResponse> addSerial(Serial serial);

    ResponseEntity<ApiResponse> updateSerial(Long serialId, Serial serial);

    ResponseEntity<ApiResponse> deleteSerial(Long serialId);

    Serial getSerial(Long serialId);


    Page<?> getSerialPage(int page, int size);
}
