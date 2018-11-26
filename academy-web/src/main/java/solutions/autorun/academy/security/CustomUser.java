package solutions.autorun.academy.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

    private final Long id;

    public Long getId(){
        return this.id;
    }

    public CustomUser(String username, String password,
                      Collection<? extends GrantedAuthority> authorities, Long id) {
        super(username, password, authorities);
        this.id = id;
    }
}

