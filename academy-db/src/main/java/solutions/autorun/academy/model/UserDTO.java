package solutions.autorun.academy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import solutions.autorun.academy.register.ValidEmail;

@Data
public class UserDTO {
    private String username;
    private String password;
    @ValidEmail
    private String email;
}
