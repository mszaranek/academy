package solutions.autorun.academy.services;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.repositories.ProjectRepository;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EntityManager entityManager;

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

    @Override
    public Set<Task> getTasks(Long id){
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QProject qProject = QProject.project;
        QSystem qSystem = QSystem.system;
        QTask qTask = QTask.task;

//        HashSet<Tuple> tuples = new HashSet<>(query
//                .from(qTask)
//                .select(qTask.id, qTask.number, qTask.user.id, qTask.estimate, qTask.status, qTask.type)
//                .join(qTask.system, qSystem)
//                .on(qTask.system.id.eq(qSystem.id))
//                .join(qSystem.projects, qProject)
//                .on(qSystem.projects.any().id.eq(qProject.id))
//                .where(qProject.id.eq(id))
//                .fetch());
//
//        Set<Task> tasks = new HashSet<>();
//        for (Tuple t : tuples) {
//            tasks.add(Task.builder()
//                    .id(t.get(qTask.id))
//                    .number(t.get(qTask.number))
//                    .user.builder().id(t.get(qTask.id))
//                    .estimate(t.get(qTask.estimate))
//                    .status(t.get(qTask.status))
//                    .type(t.get(qTask.type))
//                    .build());
//        }
//        return tasks;

       return new HashSet<>(query
                .from(qTask)
//                .select(qTask.id, qTask.number, qTask.user.id, qTask.estimate, qTask.status, qTask.type)
                .join(qTask.system, qSystem)
                .on(qTask.system.id.eq(qSystem.id))
                .join(qSystem.projects, qProject)
                .on(qSystem.projects.any().id.eq(qProject.id))
                .where(qProject.id.eq(id))
                .fetch());
    }
}
