package it.live.brainbox.entity;

import it.live.brainbox.entity.temp.AbsLongEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Video extends AbsLongEntity {
    @NotNull(message = "Link kiriting")
    private String link;
    @NotNull(message = "Image Link")
    private String imageLink;
}
