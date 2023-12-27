package it.live.brainbox.controller;

import it.live.brainbox.entity.enums.Genre;
import it.live.brainbox.entity.enums.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/enum")
@RequiredArgsConstructor
public class EnumController {
    @GetMapping("/genre")
    public List<Genre> genres(){return Arrays.stream(Genre.values()).toList();}
    @GetMapping("/level")
    public List<Level> levels() {
        return Arrays.stream(Level.values()).toList();
    }
}
