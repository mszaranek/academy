package solutions.autorun.academy.security;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.repositories.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }





    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);

        if (new EmailValidator().isValid(username, null)) {
            return userRepository.findOneWithAppRolesByEmail(username)
                    .map(user -> createSpringSecurityUser(username, user))
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " was not found in the database"));
        }

//        String lowercaseLogin = username.toLowerCase(Locale.ENGLISH);
        return userRepository.findOneWithAppRolesByUsername(username)
                .map(user -> createSpringSecurityUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the database"));

    }

    private CustomUser createSpringSecurityUser(String lowercaseLogin, User user) {
//        if (!user) {
//            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
//        }
        List<GrantedAuthority> grantedAuthorities = user.getAppRoles().stream()
                .map(appRole -> new SimpleGrantedAuthority(appRole.getRoleName()))
                .collect(Collectors.toList());
        return new CustomUser(user.getUsername(),
                user.getPassword(),
                grantedAuthorities,user.getId());
    }
}
