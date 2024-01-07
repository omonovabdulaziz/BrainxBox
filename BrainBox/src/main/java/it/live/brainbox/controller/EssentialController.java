package it.live.brainbox.controller;

import it.live.brainbox.entity.EssentialWords;
import it.live.brainbox.repository.EssentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/essential")
@RequiredArgsConstructor
public class EssentialController {
    private final EssentialRepository essentialRepository;

    @GetMapping("/{book}/{unit}")
    public List<EssentialWords> getEssentialWords(@PathVariable Integer book, @PathVariable Integer unit) {
        return essentialRepository.findAllByBookIdAndUnitId(book, unit);
    }
}
