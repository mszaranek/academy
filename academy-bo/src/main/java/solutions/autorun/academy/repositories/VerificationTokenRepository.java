package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
}
