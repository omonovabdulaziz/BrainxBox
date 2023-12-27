package it.live.brainbox.mapper;

import it.live.brainbox.entity.Language;
import it.live.brainbox.payload.LanguageDTO;
import org.springframework.stereotype.Service;

@Service

public class LanguageMapper {
    public Language toEntity(LanguageDTO languageDTO) {
        return Language.builder().name(languageDTO.getName()).build();
    }
}
