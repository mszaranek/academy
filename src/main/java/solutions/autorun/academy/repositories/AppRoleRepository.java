package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
}
