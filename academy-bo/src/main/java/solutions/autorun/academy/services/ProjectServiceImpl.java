package solutions.autorun.academy.services;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.model.System;
import solutions.autorun.academy.repositories.InvoiceRepository;
import solutions.autorun.academy.repositories.ProjectRepository;
import solutions.autorun.academy.repositories.SystemRepository;
import solutions.autorun.academy.repositories.UserRepository;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final SystemRepository systemRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
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
    public void addUserToProject(Long id, Long userId){
        User user = userRepository.findById(userId).get();
        Project project = findProjectById(id);
        user.getProjects().add(project);
        userRepository.save(user);
    }

    @Override
    public void addInvoiceToProject(Long id, Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        invoice.getProjects().add(findProjectById(id));
        invoiceRepository.save(invoice);
    }

    @Override
    public void addSystemToProject(Long id, Long systemId) {
        System system = systemRepository.findById(systemId).get();
        system.getProjects().add(findProjectById(id));
        systemRepository.save(system);
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
    public Set<Task> getTasks(Long id) {
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QProject qProject = QProject.project;
        QSystem qSystem = QSystem.system;
        QTask qTask = QTask.task;

        return new HashSet<>(query
                .from(qTask)
                .join(qTask.system, qSystem)
                .on(qTask.system.id.eq(qSystem.id))
                .join(qSystem.projects, qProject)
                .on(qSystem.projects.any().id.eq(qProject.id))
                .where(qProject.id.eq(id))
                .fetch());
    }
}
