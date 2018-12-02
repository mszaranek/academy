package solutions.autorun.academy.security.jwt;

//import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import solutions.autorun.academy.security.CustomUser;
import solutions.autorun.academy.security.exceptions.UserNotActivatedException;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
//
    @Value("${solutions.autorun.academy.key}")
   private String keyString;

    private Key key;
    @Value("${solutions.autorun.academy.validity}")
    private int validity;
//
////    private long tokenValidityInMilliseconds;
//
////    private long tokenValidityInMillisecondsForRememberMe;
//
//
    public TokenProvider() {

    }
//
    @PostConstruct
    public void init() {
     //  byte[] keyBytes;
//        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
//        if (!StringUtils.isEmpty(secret)) {
//            log.warn("Warning: the JWT key used is not Base64-encoded. " +
//                    "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
//            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
//        } else {
//            log.debug("Using a Base64-encoded JWT secret key");
          // keyBytes = Decoders.BASE64.decode(this.keyString);

    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.keyString));
//        this.tokenValidityInMilliseconds =
//                1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
//        this.tokenValidityInMillisecondsForRememberMe =
//                1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt()
//                        .getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication) {
        try {
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            CustomUser customUser = (CustomUser) authentication.getPrincipal();

            return Jwts.builder()
                    .setSubject(authentication.getName())
                    .setId(customUser.getId().toString())
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setExpiration(new Date((new Date()).getTime() + validity))
                    .compact();
        }
        catch(UserNotActivatedException e){
            return "User not activated";
        }
    }



    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            //System.out.println(Jwts.parser().setSigningKey(key).parseClaimsJws(authToken));
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
