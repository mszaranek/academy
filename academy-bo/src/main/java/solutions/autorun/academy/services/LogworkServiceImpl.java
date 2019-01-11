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
import java.time.Month;
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
        logWork.setStatus("inProgress");
        logWork.setVerifyMethodUsed(null);
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
    public LogWork updateLogworkByManager(Long id, LogWorkDTO logWorkDTO, Long taskId) {
        LogWork logWork = logworkRepository.findById(id).get();
        logWork.setWorkedTime(logWorkDTO.getWorkedTime());
        logWork.setDescription(logWorkDTO.getDescription());
        logWork.setDate(localDateConverter.createDate(logWorkDTO.getDate()));
        logWork.setTask(taskService.findTaskById(taskId));
        logWork.setStatus("corrected");
        logWork.setVerifyMethodUsed(null);
        return logworkRepository.save(logWork);
    }

    @Override
    public Set<LogWork> getUserLogworkWeek(Long id, LocalDate localDate, boolean manager) {
        JPAQuery<LogWork> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QLogWork qLogWork = QLogWork.logWork;
        HashSet<LogWork> logWorks;
        if (manager){
            logWorks = new HashSet<>(query
                    .from(qLogWork)
                    .join(qLogWork.user, qUser)
                    .on(qLogWork.user.id.eq(qUser.id))
                    .where(qUser.id.eq(id), qLogWork.date.between(localDate.with(DayOfWeek.MONDAY), localDate.with(DayOfWeek.SUNDAY)), qLogWork.status.notEqualsIgnoreCase("inProgress"))
                    .fetch());
        }
        else{
            logWorks = new HashSet<>(query
                    .from(qLogWork)
                    .join(qLogWork.user, qUser)
                    .on(qLogWork.user.id.eq(qUser.id))
                    .where(qUser.id.eq(id), qLogWork.date.between(localDate.with(DayOfWeek.MONDAY), localDate.with(DayOfWeek.SUNDAY)))
                    .fetch());
        }
        return logWorks;
    }

    @Override
    public Set<LogWork> getUserLogworkMonth(Long id, LocalDate localDate, boolean manager) {
        JPAQuery<LogWork> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QLogWork qLogWork = QLogWork.logWork;
        HashSet<LogWork> logWorks;
        if (manager){
            logWorks = new HashSet<>(query
                    .from(qLogWork)
                    .join(qLogWork.user, qUser)
                    .on(qLogWork.user.id.eq(qUser.id))
                    .where(qUser.id.eq(id), qLogWork.date.between(localDate.withDayOfMonth(1), localDate.withDayOfMonth(localDate.lengthOfMonth())), qLogWork.status.notEqualsIgnoreCase("inProgress"))
                    .fetch());
        }
        else{
            logWorks = new HashSet<>(query
                    .from(qLogWork)
                    .join(qLogWork.user, qUser)
                    .on(qLogWork.user.id.eq(qUser.id))
                    .where(qUser.id.eq(id), qLogWork.date.between(localDate.withDayOfMonth(1), localDate.withDayOfMonth(localDate.lengthOfMonth())))
                    .fetch());
        }
        return logWorks;
    }

    @Override
    public Set<LogWork> getUserLogworkDay(Long id, LocalDate localDate, boolean manager) {
        JPAQuery<LogWork> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QLogWork qLogWork = QLogWork.logWork;
        HashSet<LogWork> logWorks;
        if(manager){
            logWorks = new HashSet<>(query
                    .from(qLogWork)
                    .join(qLogWork.user, qUser)
                    .on(qLogWork.user.id.eq(qUser.id))
                    .where(qUser.id.eq(id), qLogWork.date.eq(localDate), qLogWork.status.notEqualsIgnoreCase("inProgress"))
                    .fetch());
        }
        else{
            logWorks = new HashSet<>(query
                    .from(qLogWork)
                    .join(qLogWork.user, qUser)
                    .on(qLogWork.user.id.eq(qUser.id))
                    .where(qUser.id.eq(id), qLogWork.date.eq(localDate))
                    .fetch());
        }
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
    public Set<LogWork> sendToValidation(Long id, LocalDate localDate, boolean weekly) {
        Set<LogWork> logWorks = new HashSet<>();
        String verifyStatus = null;
        if(weekly){
            logWorks = getUserLogworkWeek(id, localDate, false);
            verifyStatus = "week";
        }
        else if (!weekly){
            logWorks = getUserLogworkMonth(id, localDate, false);
            verifyStatus = "month";
        }

        for (LogWork logWork: logWorks) {
            if(logWork.getStatus().equals("inProgress")){
                logWork.setStatus("sentToValidation");
                logWork.setVerifyMethodUsed(verifyStatus);
                logworkRepository.save(logWork);
            }
        }
        return logWorks;
    }

    public Set<LogWork> sendDayToValidation(Long id, LocalDate localDate) {
        Set<LogWork> logWorks = getUserLogworkDay(id, localDate, false);

        for (LogWork logWork: logWorks) {
            logWork.setStatus("sentToValidation");
            logWork.setVerifyMethodUsed("day");
            logworkRepository.save(logWork);
        }
        return logWorks;
    }

    @Override
    public Set<LogWork> acceptLogworks(Long id, LocalDate localDate, boolean weekly, Long userId, String status) {
        Set<LogWork> logWorks = new HashSet<>();
        if(weekly){
            logWorks = getUserLogworkWeek(userId, localDate, true);
        }
        else if (!weekly){
            logWorks = getUserLogworkMonth(userId, localDate, true);
        }

        for (LogWork logWork: logWorks) {
            logWork.setStatus(status);
            logworkRepository.save(logWork);
        }
        return logWorks;
    }

    @Override
    public Set<LogWork> acceptDayLogworks(Long id, LocalDate localDate, Long userId, String status) {
        Set<LogWork> logWorks = getUserLogworkDay(userId, localDate, true);
        for (LogWork logWork: logWorks){
            logWork.setStatus(status);
            logworkRepository.save(logWork);
        }
        return logWorks;
    }

    @Override
    public Set<LogWork> getUserLogwork(Long id, LocalDate localDate, boolean weekly) {
        Set<LogWork> logWorks;
        if(weekly){
            logWorks = getUserLogworkWeek(id, localDate, false);
        }
        else{
            logWorks = getUserLogworkMonth(id, localDate, false);
        }
        return  logWorks;
    }

    @Override
    public Set<LogWork> getLogworksOfUser(Long id, LocalDate localDate, boolean weekly) {
        Set<LogWork> logWorks;
        if(weekly){
            logWorks = getUserLogworkWeek(id, localDate, true);
        }
        else{
            logWorks = getUserLogworkMonth(id, localDate, true);
        }
        return  logWorks;
    }

    @Override
    public void deleteLogwork(Long id) {
        logworkRepository.delete(logworkRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("logwork not found"))));
    }
}
