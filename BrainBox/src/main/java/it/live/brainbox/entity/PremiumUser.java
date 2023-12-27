package it.live.brainbox.entity;

import it.live.brainbox.entity.temp.AbsLongEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PremiumUser extends AbsLongEntity {
    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private LocalDate premiumDate;
}
