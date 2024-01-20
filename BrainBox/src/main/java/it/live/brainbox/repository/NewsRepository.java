package it.live.brainbox.repository;

import it.live.brainbox.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Long> {
    Boolean existsByTitle(String title);

    @Query("SELECT n FROM News n ORDER BY n.createdAt DESC")
    News findLatestNewsWithLimit();
}
