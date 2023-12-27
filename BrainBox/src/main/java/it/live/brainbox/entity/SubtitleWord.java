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
    @ManyToOne
    @JsonIgnore
    private Language language;
    private Integer count;
    private String pronunciation;
    @Column(columnDefinition = "text")
    private String definition;
    private String secondLanguageValue;
}
