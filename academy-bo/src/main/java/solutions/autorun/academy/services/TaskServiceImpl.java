package solutions.autorun.academy.services;

import com.fasterxml.jackson.annotation.JsonView;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.Estimate;
import solutions.autorun.academy.model.QTask;
import solutions.autorun.academy.model.QUser;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.repositories.EstimateRepository;
import solutions.autorun.academy.repositories.TaskRepository;
import solutions.autorun.academy.repositories.UserRepository;
import solutions.autorun.academy.views.Views;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;

    @Override
    public void saveTasks(Set<Task> tasks){
        taskRepository.saveAll(tasks);
    }

    @Override
    @JsonView(Views.TaskView.class)
    public Page<Task> getTasksForEstimation(Long userId, Pageable pageable){
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QTask qTask = QTask.task;
        QUser qUser = QUser.user;
        List<Task> tasks = new ArrayList<>(query
                .from(qTask)
                .join(qTask.users, qUser)
                .where(qUser.in(qTask.users))
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

    @Override
    public void addEstimate(Long taskId, Long userId, Integer value){

        Estimate estimate;
        if(userRepository.findById(userId).get().getTasks().stream().anyMatch(task -> task.getEstimates().stream().anyMatch(estimate1 -> estimate1.getUser().getId()==userId))){
            estimate = estimateRepository.findByUserAndTask(userRepository.findById(userId).get(),taskRepository.findById(taskId).get()).get();
            estimate.setValue(value);
        }
        else {
         estimate = new Estimate(userRepository.findById(userId).get(), taskRepository.findById(taskId).get(), value);
        }
        estimateRepository.save(estimate);
    }

    @Override
    public Estimate getEstimate(Long taskId, Long userId){
        try {
            return estimateRepository.findByUserAndTask(userRepository.findById(userId).get(), taskRepository.findById(taskId).get()).get();
        }
        catch (NoSuchElementException e){
            return null;
        }
    }
}
