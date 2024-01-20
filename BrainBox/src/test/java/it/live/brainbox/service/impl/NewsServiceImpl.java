package it.live.brainbox.service.impl;

import it.live.brainbox.config.SecurityConfiguration;
import it.live.brainbox.entity.News;
import it.live.brainbox.entity.User;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.mapper.SeenNewsMapper;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.repository.NewsRepository;
import it.live.brainbox.repository.SeenNewsRepository;
import it.live.brainbox.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.springframework.data.domain.PageRequest.of;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final SeenNewsRepository seenNewsRepository;
    private final SeenNewsMapper seenNewsMapper;


    public ResponseEntity<ApiResponse> add(String title, String dialog, MultipartFile multipartFile) {
        if (newsRepository.existsByTitle(title)) throw new MainException("Bunday title mavjud");

        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        News news = News.builder().title(title).dialog(dialog).build();
        try {
            Path uploadDirectory = Paths.get("document");
            if (!Files.exists(uploadDirectory)) Files.createDirectories(uploadDirectory);
            Path filePath = uploadDirectory.resolve(fileName);
            multipartFile.transferTo(filePath);
            news.setImageUrl(filePath.toAbsolutePath().toString());
        } catch (Exception e) {
            throw new MainException("Image Upload Exception");
        }
        newsRepository.save(news);
        return ResponseEntity.ok(ApiResponse.builder().status(200).message("Ok").build());
    }

    @Override
    public Page<News> get(int page, int size) {
        return newsRepository.findAll(of(page, size, Sort.by("createdAt").descending()));
    }


    public ResponseEntity<ApiResponse> update(String title, String dialog, Long id, MultipartFile multipartFile) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NotFoundException("Not Found"));
        Path existingFilePath = Paths.get(news.getImageUrl());
        if (Files.exists(existingFilePath)) {
            try {
                Files.delete(existingFilePath);
            } catch (IOException e) {
                throw new MainException("Delete problem");
            }
        }

        try {
            String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
            Path uploadDirectory = Paths.get("document");
            if (!Files.exists(uploadDirectory)) Files.createDirectories(uploadDirectory);

            Path filePath = uploadDirectory.resolve(fileName);
            multipartFile.transferTo(filePath);
            news.setImageUrl(filePath.toAbsolutePath().toString());
            news.setDialog(dialog);
            news.setTitle(title);
            newsRepository.save(news);
            return ResponseEntity.ok(ApiResponse.builder().status(200).message("Updated").build());
        } catch (IOException e) {
            throw new MainException("Xatolik");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addSeen(Long newsId) {
        User systemUser = SecurityConfiguration.getOwnSecurityInformation();
        if (seenNewsRepository.existsByUserIdAndNewsId(systemUser.getId(), newsId))
            throw new MainException("Already added");
        seenNewsRepository.save(seenNewsMapper.toEntity(newsId, systemUser));
        return ResponseEntity.ok(ApiResponse.builder().status(200).message("Seen added").build());
    }

    @Override
    public News getLastNews() {
        User systemUser = SecurityConfiguration.getOwnSecurityInformation();
        News news = newsRepository.findLatestNewsWithLimit();
        if (!seenNewsRepository.existsByUserIdAndNewsId(systemUser.getId(), news.getId())) return news;
        return News.builder().build();
    }
}
