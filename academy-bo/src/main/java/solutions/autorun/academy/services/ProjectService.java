package solutions.autorun.academy.services;

import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.Project;
import solutions.autorun.academy.model.Task;

import java.util.Set;

public interface ProjectService {

    Set<Project> getProjects();

    void createProject(Project project);

    Project findProjectById(Long id);

    void updateProject(Project project);

    void deleteProject(Long id);

    Set<Task> getTasks(Long id);
}
