package it.live.brainbox.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class SavedWordDto {
    private UUID id;
    private String value;
    private String translateWord;
}
