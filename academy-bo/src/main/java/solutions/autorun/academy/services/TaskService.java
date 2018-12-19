package solutions.autorun.academy.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solutions.autorun.academy.model.Task;

import java.util.Set;

public interface TaskService {

    void saveTasks(Set<Task> tasks);

    Task findTaskById(Long id);

    Page<Task> getTasksForEstimation(Long userId, Pageable pageable);

    void addEstimate(Long taskId, Long userId, Integer value);
}
