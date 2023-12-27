package it.live.brainbox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.live.brainbox.entity.temp.AbsLongEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Serial extends AbsLongEntity {
    private String name;
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "serial", cascade = CascadeType.ALL)
    private List<Movie> movies;
}
