package solutions.autorun.academy.exceptions;

public class UsernameExistsException extends Exception{

    public UsernameExistsException() {
        super();
    }

    public UsernameExistsException(String message) {
        super(message);
    }
}
