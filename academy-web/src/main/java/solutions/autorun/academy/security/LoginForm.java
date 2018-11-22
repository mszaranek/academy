package solutions.autorun.academy.security;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginForm {

    @NotNull
    private String username;
    @NotNull
    private String password;
}
