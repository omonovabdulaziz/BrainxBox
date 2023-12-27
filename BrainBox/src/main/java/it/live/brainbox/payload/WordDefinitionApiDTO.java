package it.live.brainbox.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordDefinitionApiDTO {
    private String word;
    private List<WordDefClass> definitions;
}

@Data@AllArgsConstructor@NoArgsConstructor@Builder
class WordDefClass{
    private String definition;
    private String partOfSpeech;
}