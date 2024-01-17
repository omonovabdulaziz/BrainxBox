package it.live.brainbox.repository;

import it.live.brainbox.entity.Movie;
import it.live.brainbox.entity.enums.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findAllByGenre(String genre, PageRequest pageRequest);

    Page<Movie> findAllByLevel(Level level, PageRequest pageRequest);

    List<Movie> findAllBySerialId(Long serial_id);

    List<Movie> findAllByNameLikeIgnoreCase(String name);

    Boolean existsByNameIgnoreCase(String name);
}
