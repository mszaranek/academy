package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
