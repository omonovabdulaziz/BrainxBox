package it.live.brainbox.controller;

import it.live.brainbox.entity.Serial;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.service.SerialService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/serial")
@RestController
public class SerialController {
    private final SerialService serialService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addSerial")
    public ResponseEntity<ApiResponse> addSerial(@RequestBody Serial serial) {
        return serialService.addSerial(serial);
    }


    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateSerial/{serialId}")
    public ResponseEntity<ApiResponse> updateSerial(@PathVariable Long serialId, @RequestBody Serial serial) {
        return serialService.updateSerial(serialId, serial);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteSerial/{serialId}")
    public ResponseEntity<ApiResponse> deleteSerial(@PathVariable Long serialId) {
        return serialService.deleteSerial(serialId);
    }

    @GetMapping("/getSerialById/{serialId}")
    public Serial getSerial(@PathVariable Long serialId) {
        return serialService.getSerial(serialId);
    }

    @GetMapping("/getSerialsPage")
    public Page<?> getSerialPage(@RequestParam int page, @RequestParam int size){
        return serialService.getSerialPage(page ,size);
    }
}

