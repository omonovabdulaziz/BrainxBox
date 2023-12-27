package it.live.brainbox.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pronunciation {
    private Object word;
    private Pronoun pronunciation;
}

@Data@AllArgsConstructor@NoArgsConstructor@Builder
class Pronoun {
    private String all;
}