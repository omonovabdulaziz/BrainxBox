package it.live.brainbox.payload;

import it.live.brainbox.payload.SubtitleWordPyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    private List<SubtitleWordPyDTO> result;
}
