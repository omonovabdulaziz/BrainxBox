package it.live.brainbox.config;

import it.live.brainbox.entity.Language;
import it.live.brainbox.entity.User;
import it.live.brainbox.entity.enums.SystemRoleName;
import it.live.brainbox.jwt.JwtProvider;
import it.live.brainbox.repository.LanguageRepository;
import it.live.brainbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DataLoaderConfig implements CommandLineRunner {
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;


    @Value("${spring.sql.init.mode}")
    private String sqlInitMode;

    @Override
    public void run(String... args) throws Exception {
        if (Objects.equals(sqlInitMode, "always")) {
            userRepository.save(User.builder().isPremium(true).name("Abdulaziz").email("omonov2006omonov@gmail.com").surname("Omonov").systemRoleName(SystemRoleName.ROLE_ADMIN).enabled(true).isAccountNonExpired(true).isAccountNonLocked(true).isCredentialsNonExpired(true).uniqueId("omonov2006").coins(0).build());
            languageRepository.save(Language.builder().name("ENGLISH").build());
            languageRepository.save(Language.builder().name("RUSSIAN").build());
            userRepository.save(User.builder().isPremium(false).name("Developer").email("dev@gmail.com").surname("Developer").systemRoleName(SystemRoleName.ROLE_USER).enabled(true).isAccountNonExpired(true).isAccountNonLocked(true).isCredentialsNonExpired(true).uniqueId("dev").coins(0).build());
            System.out.println("MALUMOTLAR SAQLANDI");
        }
    }


}
