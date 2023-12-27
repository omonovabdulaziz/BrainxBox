package it.live.brainbox.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtitleWordPyDTO {
    private String word;
    private Integer count;
    private String translation;

}
