package solutions.autorun.academy.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.Project;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.services.ProjectService;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(value = "/projects")
    public ResponseEntity<Set<Project>> showProjects() {
        long startTime = System.currentTimeMillis();
        Set<Project> projects = projectService.getProjects();
        long estimatedTime = (System.currentTimeMillis() - startTime)/1000;
        System.out.println("Time: "+estimatedTime);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping(value = "/projects")
    public ResponseEntity<Void> createProject(@RequestBody Project project) {
        projectService.createProject(project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "project/{id}")
    public ResponseEntity<Project> findProject(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.findProjectById(id), HttpStatus.OK);
    }

    @PutMapping(value = "/projects")
    public ResponseEntity<Void> updateProject(@RequestBody Project project) {
        projectService.findProjectById(project.getId());
        projectService.updateProject(project);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "project/{id}/invoices")
    public ResponseEntity<Set<Invoice>> findProjectsInvoices(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.findProjectById(id).getInvoices(), HttpStatus.OK);
    }

    @GetMapping(value = "project/{id}/tasks")
    public ResponseEntity<Set<Task>> findProjectsTasks(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.getTasks(id), HttpStatus.OK);
    }
}
