package it.live.brainbox.entity;

import it.live.brainbox.entity.temp.AbsLongEntity;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class EssentialWords extends AbsLongEntity {
    private Integer bookId;
    private Integer unitId;
    private String word;
    private String translation_en;
    private String translation_ru;
}
