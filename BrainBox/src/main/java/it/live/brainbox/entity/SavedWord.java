package it.live.brainbox.entity;

import it.live.brainbox.entity.temp.AbsUUIDEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SavedWord extends AbsUUIDEntity {
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private String value;
    @Column(nullable = false)
    private String translateWord;
}
