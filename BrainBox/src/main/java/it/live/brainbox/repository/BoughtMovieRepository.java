package it.live.brainbox.repository;

import it.live.brainbox.entity.BoughtMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoughtMovieRepository extends JpaRepository<BoughtMovie , UUID> {
   Boolean existsByUserIdAndMovieId(Long user_id, Long movie_id);
}
