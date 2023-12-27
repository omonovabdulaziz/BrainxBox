package it.live.brainbox.entity;

import it.live.brainbox.entity.temp.AbsUUIDEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BoughtMovie extends AbsUUIDEntity {
    @ManyToOne
    private User user;
    @ManyToOne
    private Movie movie;
}
