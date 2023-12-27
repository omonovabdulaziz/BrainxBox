package it.live.brainbox.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoughtMovieDTO {
    private Long userId;
    private Long movieId;
}
