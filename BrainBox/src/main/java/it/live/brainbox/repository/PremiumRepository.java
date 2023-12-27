package it.live.brainbox.repository;

import it.live.brainbox.entity.PremiumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PremiumRepository extends JpaRepository<PremiumUser, Long> {
    @Query(value = "SELECT * FROM premium_user WHERE premium_date < :cutoffDate", nativeQuery = true)
    List<PremiumUser> findPremiumUsersBeforeCutoffDate(@Param("cutoffDate") LocalDate cutoffDate);

}
