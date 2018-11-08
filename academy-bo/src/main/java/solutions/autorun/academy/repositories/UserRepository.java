package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"tasks", "invoices","projects","appRoles"})
    List<User> findAll();
   @EntityGraph(attributePaths = {"tasks", "invoices","projects","appRoles"})
    Optional<User> findById(Long aLong);
}
