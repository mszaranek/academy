package solutions.autorun.academy.services;

import solutions.autorun.academy.model.Task;

import java.util.Set;

public interface TaskService {

    void saveTasks(Set<Task> tasks);
}
