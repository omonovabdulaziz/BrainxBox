package it.live.brainbox.entity;

import it.live.brainbox.entity.temp.AbsLongEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(callSuper = true)
public class SeenNews extends AbsLongEntity {
    @ManyToOne
    private User user;
    @ManyToOne
    private News news;
}
