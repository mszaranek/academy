package solutions.autorun.academy.services;

import solutions.autorun.academy.model.LogWork;
import solutions.autorun.academy.model.LogWorkDTO;

import javax.mail.search.SearchTerm;
import java.time.LocalDate;
import java.util.Set;

public interface LogworkService {

    LogWork createLogwork(Long id, LogWorkDTO logWork, Long taskId);

    LogWork updateLogwork(Long id, LogWorkDTO logWork, Long taskId);

    Set<LogWork> getUserLogwork(Long id, LocalDate localDate, boolean weekly);

    Set<LogWork> getUserLogworkWeek(Long id, LocalDate localDate);

    Set<LogWork> getUserLogworkDay(Long id, LocalDate localDate);

    Set<LogWork> getUserLogworkMonth(Long id, LocalDate localDate);

    Set<LogWork> getUsersLogworksInProject(Long id, LocalDate localDate, boolean weekly);

    Set<LogWork> sendToValidation(Long id, LocalDate localDate, boolean weekly);

    Set<LogWork> sendDayToValidation(Long id, LocalDate localDate);

    void deleteLogwork(Long id);

}
