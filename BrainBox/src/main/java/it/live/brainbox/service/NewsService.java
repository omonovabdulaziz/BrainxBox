package it.live.brainbox.service;

import it.live.brainbox.entity.News;
import it.live.brainbox.payload.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;


public interface NewsService {

    ResponseEntity<ApiResponse> add(String title , String dialog  , MultipartFile multipartFile);

    Page<News> get(int page, int size);

    ResponseEntity<ApiResponse> update(String title , String dialog, Long id , MultipartFile multipartFile);

    ResponseEntity<ApiResponse> addSeen(Long newsId);

    News getLastNews();

    ResponseEntity<?> viewOneFile(String path) throws MalformedURLException;
}
