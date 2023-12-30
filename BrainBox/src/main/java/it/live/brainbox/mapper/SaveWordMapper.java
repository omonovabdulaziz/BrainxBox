package it.live.brainbox.mapper;

import it.live.brainbox.entity.SavedWord;
import it.live.brainbox.payload.SavedWordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveWordMapper {

    public SavedWordDto toDTO(SavedWord savedWord) {
        return SavedWordDto.builder()
                .id(savedWord.getId())
                .value(savedWord.getValue()).build();
    }
}
