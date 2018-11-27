package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
