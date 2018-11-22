package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value="userEntityGraph")
    List<User> findAll();

   @EntityGraph(value="userEntityGraph")
    Optional<User> findById(Long aLong);

    @EntityGraph(value="userEntityGraph")
    Optional<User> findByEmail(String email);

    @EntityGraph(value="userEntityGraph")
    Optional<User> findByUsername(String username);
}
