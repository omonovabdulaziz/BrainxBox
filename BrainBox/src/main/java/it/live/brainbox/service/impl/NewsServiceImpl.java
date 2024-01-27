package it.live.brainbox.service.impl;

import com.sun.tools.javac.Main;
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
import org.springframework.core.io.FileUrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    private static final String MAIN_UPLOAD_DIRECTORY = "documents";
    private static final String BaseUrl = "http://137.184.14.168:8080/";

    public ResponseEntity<ApiResponse> add(String title, String dialog, MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        News news = News.builder().title(title).dialog(dialog).build();
        try {
            Path uploadDirectory = Paths.get(MAIN_UPLOAD_DIRECTORY);
            if (!Files.exists(uploadDirectory)) Files.createDirectories(uploadDirectory);
            Path filePath = uploadDirectory.resolve(fileName);
            multipartFile.transferTo(filePath);
            news.setImageUrl(BaseUrl + "api/v1/news/image?path=" + MAIN_UPLOAD_DIRECTORY + File.separator + fileName);
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
            Path uploadDirectory = Paths.get(MAIN_UPLOAD_DIRECTORY);
            if (!Files.exists(uploadDirectory)) Files.createDirectories(uploadDirectory);

            Path filePath = uploadDirectory.resolve(fileName);
            multipartFile.transferTo(filePath);
            news.setImageUrl(BaseUrl + "api/v1/news&path=" + MAIN_UPLOAD_DIRECTORY + File.separator + fileName);
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
        News news = newsRepository.findFirstByOrderByCreatedAtDesc();
        if (!seenNewsRepository.existsByUserIdAndNewsId(systemUser.getId(), news.getId())) return news;
        return News.builder().build();
    }

    @Override
    public ResponseEntity<?> viewOneFile(String path) throws MalformedURLException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(path, StandardCharsets.UTF_8)).
                body(new FileUrlResource(path));
    }


}
