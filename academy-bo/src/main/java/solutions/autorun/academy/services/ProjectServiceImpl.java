package solutions.autorun.academy.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.Project;
import solutions.autorun.academy.repositories.ProjectRepository;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Set<Project> getProjects() {
        return new HashSet<>(projectRepository.findAll());
    }

    @Override
    public void createProject(Project project) {
        project.setId(null);
        projectRepository.save(project);
    }

    @Override
    public Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("Project not found")));
    }

    @Override
    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.delete(projectRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("Project not found"))));
    }
}
