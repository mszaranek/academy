package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
