package it.live.brainbox.repository;

import it.live.brainbox.entity.SavedWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavedWordRepository extends JpaRepository<SavedWord , UUID> {
    List<SavedWord> findAllByUserId(Long user_id);
}
