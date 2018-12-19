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

    Set<LogWork> getUsersLogworksInProject(Long id, LocalDate localDate, boolean weekly);

    void deleteLogwork(Long id);

}
