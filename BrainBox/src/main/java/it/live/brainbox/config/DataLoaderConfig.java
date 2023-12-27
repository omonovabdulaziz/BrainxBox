package it.live.brainbox.config;

import it.live.brainbox.entity.User;
import it.live.brainbox.entity.enums.SystemRoleName;
import it.live.brainbox.jwt.JwtProvider;
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
    private final JwtProvider jetProvider;


    @Value("${spring.sql.init.mode}")
    private String sqlInitMode;

    @Override
    public void run(String... args) throws Exception {
        if (Objects.equals(sqlInitMode, "always")) {
            userRepository.save(User.builder().isPremium(true).name("Abdulaziz").email("omonov2006omonov@gmail.com").surname("Omonov").systemRoleName(SystemRoleName.ROLE_ADMIN).enabled(true).isAccountNonExpired(true).isAccountNonLocked(true).isCredentialsNonExpired(true).uniqueId("omonov2006").coins(0).build());
            jetProvider.generateToken("omonov2006omonov@gmail.com");
            System.out.println("_A_D_M_I_N_S_A_Q_L_A_N_D_I  " );
        }
    }


}
