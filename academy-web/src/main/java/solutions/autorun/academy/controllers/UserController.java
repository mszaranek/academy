package solutions.autorun.academy.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import solutions.autorun.academy.Account.OnRegistrationCompleteEvent;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.services.UserService;
import solutions.autorun.academy.views.Views;
import org.springframework.web.multipart.MultipartFile;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.Project;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.services.UserService;
import solutions.autorun.academy.views.Views;

import javax.transaction.Transactional;
import java.lang.System;
import java.util.Locale;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;
    private MessageSource messages;
    ApplicationEventPublisher eventPublisher;

    @JsonView(Views.UserView.class)
    @GetMapping(value = "/users")
    public ResponseEntity<Set<User>> showUsers() {
        long startTime = System.currentTimeMillis();
        Set<User> users = userService.getUsers();
        long estimatedTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Time: " + estimatedTime);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO, WebRequest request) {
        User user = userService.createUser(userDTO);
        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/registrationConfirm")
    public String confirmRegistration(WebRequest request, @RequestParam("token") String token){
        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if(verificationToken == null){
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            return message;
        }

        User user = verificationToken.getUser();
        user.setActivated(true);
        userService.saveRegisteredUser(user);
        return "User activated";

    }

    @GetMapping(value = "users/{id}")
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

    @DeleteMapping(value = "users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/invoices")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @JsonView(Views.InvoiceView.class)
    public ResponseEntity<Set<Invoice>> showUsersInvoices(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findUserById(id).getInvoices(), HttpStatus.OK);
    }

    @GetMapping(value = "users/{userId}/projects/{projectId}/tasks")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#userId)")
    @JsonView(Views.UsersTaskView.class)
    public ResponseEntity<Set<Task>> showUsersTasksInProject(@PathVariable Long userId, @PathVariable Long projectId) {

        return new ResponseEntity<>(userService.getUsersTasksInProject(userId, projectId), HttpStatus.OK);
    }

    @GetMapping(value = "users/{userId}/projects/{projectId}/invoices")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#userId)")
    @JsonView(Views.InvoiceView.class)
    public ResponseEntity<Set<Invoice>> showUsersInvoicesInProject(@PathVariable Long userId, @PathVariable Long projectId) {

        return new ResponseEntity<>(userService.findUserInvoicesInProject(userId, projectId), HttpStatus.OK);
    }

    @GetMapping(value = "users/{userId}/projects/{projectId}/tasks/{taskId}")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#userId)")
    @JsonView(Views.ProjectsTaskView.class)
    public ResponseEntity<Set<Task>> showTaskDetails(@PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long taskId) {

        return new ResponseEntity<>(userService.getTaskDetail(userId, projectId, taskId), HttpStatus.OK);
    }

    @GetMapping(value = "users/{userId}/projects")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#userId)")
    @JsonView(Views.UsersProjectsView.class)
    public ResponseEntity<Set<Project>> showUsersProjects(@PathVariable Long userId) {

        return new ResponseEntity<>(userService.findUserById(userId).getProjects(), HttpStatus.OK);
    }

    @PostMapping(value = "users/{id}/invoices/add")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @Transactional
    @JsonView(Views.InvoiceCreationFirstStepView.class)
    //@JsonView(Views.InvoiceView.class)
    public ResponseEntity<Invoice> addUsersInvoice(@PathVariable Long id, @RequestParam(value = "file") MultipartFile file, @RequestParam(value = "name") String fileName) {
        return new ResponseEntity<>(userService.addInvoice(file,fileName,id), HttpStatus.OK);
    }
}
