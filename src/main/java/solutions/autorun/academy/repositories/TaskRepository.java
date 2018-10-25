package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
