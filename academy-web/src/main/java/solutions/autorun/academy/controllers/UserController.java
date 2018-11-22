package solutions.autorun.academy.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.Project;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.services.UserService;
import solutions.autorun.academy.views.Views;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    @JsonView(Views.UserView.class)
    @GetMapping(value = "/users")
    public ResponseEntity<Set<User>> showUsers() {
        long startTime = System.currentTimeMillis();
        Set<User> users = userService.getUsers();
        long estimatedTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Time: " + estimatedTime);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "user/{id}")
    @JsonView(Views.UserView.class)
    public ResponseEntity<User> findUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<Void> updateUser(@RequestBody User user) {
        userService.findUserById(user.getId());
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "user/{id}/invoices")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @JsonView(Views.InvoiceView.class)
    public ResponseEntity<Set<Invoice>> showUsersInvoices(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findUserById(id).getInvoices(), HttpStatus.OK);
    }

    @GetMapping(value = "user/{userId}/projects/{projectId}/tasks")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#userId)")
    @JsonView(Views.UsersTaskView.class)
    public ResponseEntity<Set<Task>> showUsersTasksInProject(@PathVariable Long userId, @PathVariable Long projectId) {

        return new ResponseEntity<>(userService.getUsersTasksInProject(userId, projectId), HttpStatus.OK);
    }

    @GetMapping(value = "user/{userId}/projects/{projectId}/invoices")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#userId)")
    @JsonView(Views.InvoiceView.class)
    public ResponseEntity<Set<Invoice>> showUsersInvoicesInProject(@PathVariable Long userId, @PathVariable Long projectId) {

        return new ResponseEntity<>(userService.findUserInvoicesInProject(userId, projectId), HttpStatus.OK);
    }

    @GetMapping(value = "user/{userId}/projects/{projectId}/tasks/{taskId}")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#userId)")
    @JsonView(Views.ProjectsTaskView.class)
    public ResponseEntity<Set<Task>> showTaskDetails(@PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long taskId) {

        return new ResponseEntity<>(userService.getTaskDetail(userId, projectId, taskId), HttpStatus.OK);
    }

    @GetMapping(value = "user/{userId}/projects")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#userId)")
    @JsonView(Views.UsersProjectsView.class)
    public ResponseEntity<Set<Project>> showUsersProjects(@PathVariable Long userId) {

        return new ResponseEntity<>(userService.findUserById(userId).getProjects(), HttpStatus.OK);
    }
}
