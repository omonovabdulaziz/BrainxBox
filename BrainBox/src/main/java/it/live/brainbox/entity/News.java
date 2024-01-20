package it.live.brainbox.entity;

import it.live.brainbox.entity.temp.AbsLongEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(callSuper = true)
public class News extends AbsLongEntity {
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "text" , nullable = false)
    private String dialog;
    @Column(nullable = false)
    private String imageUrl;
}
