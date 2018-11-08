package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.ProjRole;

public interface ProjRoleRepository extends JpaRepository<ProjRole, Long> {
}
