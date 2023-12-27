package it.live.brainbox.repository;

import it.live.brainbox.entity.RequestMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestMovieRepository extends JpaRepository<RequestMovie , Long> {
}
