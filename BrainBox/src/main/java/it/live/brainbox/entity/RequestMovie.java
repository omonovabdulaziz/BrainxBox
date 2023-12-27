package it.live.brainbox.entity;

import it.live.brainbox.entity.temp.AbsLongEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
public class RequestMovie extends AbsLongEntity {
    private String name;
    @ManyToOne
    private User user;
}
