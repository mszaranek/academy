package solutions.autorun.academy.services;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.QTask;
import solutions.autorun.academy.model.QUser;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.repositories.TaskRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EntityManager entityManager;

    @Override
    public void saveTasks(Set<Task> tasks){
        taskRepository.saveAll(tasks);
    }

    @Override
    public Page<Task> getTasksForEstimation(Long userId, Pageable pageable){
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QTask qTask = QTask.task;
        QUser qUser = QUser.user;
        List<Task> tasks = new ArrayList<>(query
                .from(qTask)
                .join(qTask.user, qUser)
                .on(qTask.user.id.eq(qUser.id))
                .where(qTask.user.id.eq(userId))
                .fetch());
        int start = (int) pageable.getOffset();
        int end =  (start + pageable.getPageSize()) > tasks.size() ? tasks.size() : (start + pageable.getPageSize());
        try {
            Page<Task> page = new PageImpl<>(tasks.subList(start, end), pageable, tasks.size());

            return page;
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("Task not found")));
    }
}
