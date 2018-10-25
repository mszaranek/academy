package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
