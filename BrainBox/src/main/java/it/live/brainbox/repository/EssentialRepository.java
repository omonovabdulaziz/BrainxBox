package it.live.brainbox.repository;

import it.live.brainbox.entity.EssentialWords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EssentialRepository extends JpaRepository<EssentialWords, Long> {
    List<EssentialWords> findAllByBookIdAndUnitId(Integer bookId, Integer unitId);
}
