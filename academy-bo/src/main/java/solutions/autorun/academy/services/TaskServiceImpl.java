package solutions.autorun.academy.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.repositories.TaskRepository;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public void saveTasks(Set<Task> tasks){
        taskRepository.saveAll(tasks);
    }
}
