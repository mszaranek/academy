package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.AppRole;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
}
