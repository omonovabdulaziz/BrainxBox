package it.live.brainbox.repository;

import it.live.brainbox.entity.SubtitleWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubtitleRepository extends JpaRepository<SubtitleWord, UUID> {
    void deleteAllByMovieIdAndLanguageId(Long movie_id, Long language_id);

}
