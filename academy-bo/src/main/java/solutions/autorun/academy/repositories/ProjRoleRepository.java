package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.ProjRole;

@Repository
public interface ProjRoleRepository extends JpaRepository<ProjRole, Long> {
}
