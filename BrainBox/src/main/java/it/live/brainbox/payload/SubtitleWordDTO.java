package it.live.brainbox.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtitleWordDTO {
    private String value;
    private Long movieId;
    private Integer count;
    private Long languageId;
    private String definition;
    private String pronunciation;
    private String secondLanguageValue;
}
