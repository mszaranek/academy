package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
}
