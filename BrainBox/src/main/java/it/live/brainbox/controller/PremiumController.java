package it.live.brainbox.controller;

import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.service.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/premium")
public class PremiumController {
    private final PremiumService premiumService;



    @PutMapping("/set")
    public ResponseEntity<ApiResponse> setPremium(@RequestParam(required = false) Long userId){
       return premiumService.setPremium(userId);
    }


}
