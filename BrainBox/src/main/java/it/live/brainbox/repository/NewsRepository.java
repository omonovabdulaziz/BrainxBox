package it.live.brainbox.repository;

import it.live.brainbox.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    Boolean existsByTitle(String title);
    News findFirstByOrderByCreatedAtDesc();
}
