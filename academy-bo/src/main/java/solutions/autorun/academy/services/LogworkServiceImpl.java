package solutions.autorun.academy.services;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.Converter.LocalDateConverter;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.repositories.LogworkRepository;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class LogworkServiceImpl implements LogworkService {

    private final LogworkRepository logworkRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final EntityManager entityManager;
    private final LocalDateConverter localDateConverter;

    @Override
    public LogWork createLogwork(Long id, LogWorkDTO logWorkDTO, Long taskId) {
        LogWork logWork = new LogWork();
        logWork.setId(null);
        logWork.setDate(localDateConverter.createDate(logWorkDTO.getDate()));
        logWork.setDescription(logWorkDTO.getDescription());
        logWork.setWorkedTime(logWorkDTO.getWorkedTime());
        logWork.setUser(userService.findUserById(id));
        logWork.setTask(taskService.findTaskById(taskId));
        return logworkRepository.save(logWork);
    }
    @Override
    public LogWork updateLogwork(Long id, LogWorkDTO logWorkDTO, Long taskId) {
        LogWork logWork = logworkRepository.findById(id).get();
        logWork.setWorkedTime(logWorkDTO.getWorkedTime());
        logWork.setDescription(logWorkDTO.getDescription());
        logWork.setDate(localDateConverter.createDate(logWorkDTO.getDate()));
        logWork.setTask(taskService.findTaskById(taskId));
        return logworkRepository.save(logWork);
    }

    @Override
    public Set<LogWork> getUserLogwork(Long id, LocalDate localDate, boolean weekly) {
        JPAQuery<LogWork> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QLogWork qLogWork = QLogWork.logWork;
        HashSet<LogWork> logWorks = new HashSet<>(query
                .from(qLogWork)
                .join(qLogWork.user, qUser)
                .on(qLogWork.user.id.eq(qUser.id))
                .where(qUser.id.eq(id), qLogWork.date.between(localDate.with(DayOfWeek.MONDAY), localDate.with(DayOfWeek.SUNDAY)))
                .fetch());
        return logWorks;
    }

    @Override
    public Set<LogWork> getUsersLogworksInProject(Long projectId, LocalDate localDate, boolean weekly) {
        JPAQuery<LogWork> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QLogWork qLogWork = QLogWork.logWork;
        QProject qProject = QProject.project;
        QTask qTask = QTask.task;
        HashSet<LogWork> logWorks = new HashSet<>(query
                .from(qLogWork)
                .join(qLogWork.task, qTask)
                .on(qLogWork.task.id.eq(qTask.id))
                .join(qTask.project, qProject)
                .on(qProject.id.eq(qProject.id))
                .where(qProject.id.eq(projectId), qLogWork.date.between(localDate.with(DayOfWeek.MONDAY), localDate.with(DayOfWeek.SUNDAY)))
                .fetch());
        return null;
    }

    @Override
    public void deleteLogwork(Long id) {
        logworkRepository.delete(logworkRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("logwork not found"))));
    }
}
