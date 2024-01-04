package it.live.brainbox.payload;

import it.live.brainbox.entity.enums.Level;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDTO {
    private String name;
    private String description;
    private Level level;
    private Integer belongAge;
    private Long serialId;
    private String updateImageUrl;
    private String updateImageGenre;
}
