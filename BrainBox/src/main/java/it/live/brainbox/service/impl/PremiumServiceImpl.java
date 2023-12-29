package it.live.brainbox.service.impl;

import it.live.brainbox.config.SecurityConfiguration;
import it.live.brainbox.entity.PremiumUser;
import it.live.brainbox.entity.User;
import it.live.brainbox.entity.enums.SystemRoleName;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.repository.PremiumRepository;
import it.live.brainbox.repository.UserRepository;
import it.live.brainbox.service.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PremiumServiceImpl implements PremiumService {
    private final UserRepository userRepository;
    private final PremiumRepository premiumRepository;

    @Override
    public ResponseEntity<ApiResponse> setPremium(Long userId) {
        User systemUser = SecurityConfiguration.getOwnSecurityInformation();
        if (systemUser.getSystemRoleName() == SystemRoleName.ROLE_ADMIN && userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Bunday user topilmadi"));
            user.setIsPremium(true);
            premiumRepository.save(PremiumUser.builder().user(user).premiumDate(LocalDate.now()).build());
            return ResponseEntity.ok(ApiResponse.builder().message("success").status(200).build());
        }
        systemUser.setIsPremium(true);
        premiumRepository.save(PremiumUser.builder().user(systemUser).premiumDate(LocalDate.now()).build());
        userRepository.save(systemUser);
        return ResponseEntity.ok(ApiResponse.builder().status(200).message("success").build());
    }


    @Scheduled(cron = "0 0 8 * * ?")
    public void workPerDay() {
        LocalDate cutoffDate = LocalDate.now().minusDays(100);
        List<PremiumUser> premiumUsers = premiumRepository.findPremiumUsersBeforeCutoffDate(cutoffDate);
        for (PremiumUser premiumUser : premiumUsers) {
            User user = premiumUser.getUser();
            user.setIsPremium(false);
            userRepository.save(user);
            premiumRepository.delete(premiumUser);
        }
    }
}
