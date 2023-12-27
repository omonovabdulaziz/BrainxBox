package it.live.brainbox.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotNull(message = "Ism yoq")
    private String name;
    @NotNull(message = "Familiya yoq")
    private String surname;
    @NotNull(message = "Unique id yoq")
    private String uniqueId;
    @NotNull(message = "email yoq")
    private String email;
    private String imageUrl;
}
