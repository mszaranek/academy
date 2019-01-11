package solutions.autorun.academy.services;

import solutions.autorun.academy.model.Project;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.model.User;

import java.util.Set;

public interface ProjectService {

    Set<Project> getProjects();

    void createProject(Project project);

    Project findProjectById(Long id);

    void updateProject(Project project);

    void deleteProject(Long id);

    Set<Task> getTasks(Long id);

    void addUserToProject(Long id, Long userId);

    void addInvoiceToProject(Long id, Long invoiceId);

    void addSystemToProject(Long id, Long systemId);
}

