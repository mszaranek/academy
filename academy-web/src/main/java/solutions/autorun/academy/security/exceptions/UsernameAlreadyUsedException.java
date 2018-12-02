package solutions.autorun.academy.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyUsedException extends Exception {

    public UsernameAlreadyUsedException() {
        super();
    }

    public UsernameAlreadyUsedException(String message) {
        super(message);
    }
}
