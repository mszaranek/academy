package solutions.autorun.academy.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solutions.autorun.academy.Converter.LocalDateConverter;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.services.LogworkService;
import solutions.autorun.academy.services.ProjectService;
import solutions.autorun.academy.views.Views;

import java.lang.System;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping
public class ProjectController {

    private final ProjectService projectService;
    private final LogworkService logworkService;
    private final LocalDateConverter localDateConverter;

    @GetMapping(value = "/projects")
    public ResponseEntity<Set<Project>> showProjects() {
        long startTime = System.currentTimeMillis();
        Set<Project> projects = projectService.getProjects();
        long estimatedTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Time: " + estimatedTime);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping(value = "/projects")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> createProject(@RequestBody Project project) {
        projectService.createProject(project);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "projects/{id}")
    public ResponseEntity<Project> findProject(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.findProjectById(id), HttpStatus.OK);
    }

    @PutMapping(value = "/projects")
    public ResponseEntity<Void> updateProject(@RequestBody Project project) {
        projectService.findProjectById(project.getId());
        projectService.updateProject(project);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "projects/{id}/invoices")
    @JsonView(Views.InvoiceView.class)
    public ResponseEntity<Set<Invoice>> findProjectsInvoices(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.findProjectById(id).getInvoices(), HttpStatus.OK);
    }

    @GetMapping(value = "projects/{id}/tasks")
    @JsonView(Views.ProjectsTaskView.class)
    public ResponseEntity<Set<Task>> findProjectsTasks(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.findProjectById(id).getTasks(), HttpStatus.OK);
    }

    @GetMapping(value = "projects/{id}/logworks")
    @JsonView(Views.LogworkViewInProject.class)
    public ResponseEntity<Set<LogWork>> getUsersLogworksInProject(@PathVariable Long id, @RequestParam String date, @RequestParam boolean weekly) {
        return new ResponseEntity<>(logworkService.getUsersLogworksInProject(id, localDateConverter.createDate(date), weekly), HttpStatus.OK);
    }

    @GetMapping(value = "/projects/{id}/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> addUserToProject(@PathVariable Long id, @RequestParam Long userId) {
        projectService.addUserToProject(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/projects/{id}/invoice")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> addInvoiceToProject(@PathVariable Long id, @RequestParam Long invoiceId) {
        projectService.addInvoiceToProject(id, invoiceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/projects/{id}/system")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> addSystemToProject(@PathVariable Long id, @RequestParam Long systemId) {
        projectService.addSystemToProject(id, systemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
