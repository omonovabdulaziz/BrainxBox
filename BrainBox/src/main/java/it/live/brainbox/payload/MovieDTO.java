package it.live.brainbox.payload;

import it.live.brainbox.entity.enums.Level;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDTO {
    @NotNull(message = "Kino nomini kiriting")
    private String name;
    private String description;
    @NotNull(message = "Kino narxini kiriting")
    private Integer price;
    @NotNull(message = "Kino darajasini kiriting")
    private Level level;
    private Integer belongAge;
    private Long serialId;
    private String updateImageUrl;
    private String updateImageGenre;
}
