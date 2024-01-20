package it.live.brainbox.mapper;

import it.live.brainbox.entity.SeenNews;
import it.live.brainbox.entity.User;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeenNewsMapper {
    private final NewsRepository newsRepository;

    public SeenNews toEntity(Long newsId, User systemUser) {
        return SeenNews.builder().news(newsRepository.findById(newsId).orElseThrow(() -> new NotFoundException("Not found"))).user(systemUser).build();
    }
}
