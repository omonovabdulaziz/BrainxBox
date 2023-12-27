package it.live.brainbox.repository;


import it.live.brainbox.entity.User;
import it.live.brainbox.entity.enums.SystemRoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BinaryOperator;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndUniqueId(String email, String uniqueId);
    Page<User> findAllBySystemRoleName(SystemRoleName systemRoleName , Pageable pageable);
    Boolean existsByEmailAndUniqueIdAndSystemRoleName(String email, String uniqueId, SystemRoleName systemRoleName);

}
