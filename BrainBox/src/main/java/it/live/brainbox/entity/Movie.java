package it.live.brainbox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.live.brainbox.entity.enums.Level;
import it.live.brainbox.entity.temp.AbsLongEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends AbsLongEntity {
    @Column(nullable = false)
    private String name;
    private String description;
    private Integer price;
    private String avatarUrl;
    private String genre;
    @Transient
    private Boolean isBought;
    @Enumerated(EnumType.STRING)
    private Level level;
    private Integer belongAge;
    @ManyToOne(fetch = FetchType.EAGER)
    private Serial serial;
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<SubtitleWord> subtitleWords;
}
