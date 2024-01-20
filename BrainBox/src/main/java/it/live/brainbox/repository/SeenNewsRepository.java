package it.live.brainbox.repository;

import it.live.brainbox.entity.SeenNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeenNewsRepository extends JpaRepository<SeenNews, Long> {
    Boolean existsByUserIdAndNewsId(Long user_id, Long news_id);
}
