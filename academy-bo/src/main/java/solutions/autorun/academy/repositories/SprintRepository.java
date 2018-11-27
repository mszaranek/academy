package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.Sprint;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
}
