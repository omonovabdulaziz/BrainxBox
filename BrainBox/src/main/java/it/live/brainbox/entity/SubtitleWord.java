package it.live.brainbox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.live.brainbox.entity.temp.AbsUUIDEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SubtitleWord extends AbsUUIDEntity {
    private String value;
    @JoinColumn(nullable = false)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
    private Integer count;
    private String pronunciation;
    private String translation_en;
    private String translation_ru;

}
