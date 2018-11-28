package solutions.autorun.academy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value="userEntityGraph")
    List<User> findAll();

   @EntityGraph(value="userEntityGraph")
    Optional<User> findById(Long aLong);

    //Optional<User> findOneByActivationKey(String activationKey);

    //List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    //Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByUsername(String username);

    @EntityGraph(attributePaths = "appRoles")
    Optional<User> findOneWithAppRolesById(Long id);

    @EntityGraph(attributePaths = "appRoles")
    Optional<User> findOneWithAppRolesByUsername(String username);

    @EntityGraph(attributePaths = "appRoles")
    Optional<User> findOneWithAppRolesByEmail(String email);

    Page<User> findAllByUsernameNot(Pageable pageable, String username);


}
