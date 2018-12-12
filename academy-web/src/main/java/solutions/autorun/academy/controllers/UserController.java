package solutions.autorun.academy.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.IOUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import solutions.autorun.academy.Account.OnRegistrationCompleteEvent;
import solutions.autorun.academy.exceptions.FileManagerException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.services.InvoiceService;
import solutions.autorun.academy.services.TaskService;
import solutions.autorun.academy.services.UserService;
import solutions.autorun.academy.views.Views;
import org.springframework.web.multipart.MultipartFile;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.Project;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.services.UserService;
import solutions.autorun.academy.views.Views;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.System;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final InvoiceService invoiceService;
    private final TaskService taskService;
    private MessageSource messages;
    ApplicationEventPublisher eventPublisher;

    @JsonView(Views.UserView.class)
    @GetMapping(value = "/users")
    public ResponseEntity<Set<User>> showUsers() {
        //long startTime = System.currentTimeMillis();
        Set<User> users = userService.getUsers();
       // long estimatedTime = (System.currentTimeMillis() - startTime) / 1000;
      //  System.out.println("Time: " + estimatedTime);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO, WebRequest request) {
        User user = userService.createUser(userDTO);
        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
        }catch (Exception e){
            log.debug(e.getMessage());
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
    public ResponseEntity<Invoice> addUsersInvoice(@PathVariable Long id, @RequestParam(value = "file") MultipartFile file, @RequestParam(value = "name") String fileName) {
        try {
            return new ResponseEntity<>(invoiceService.addInvoice(file, fileName, id), HttpStatus.CREATED);
        }
        catch(FileManagerException e){
            HttpHeaders headers =  new HttpHeaders();
            headers.add("FileManagerException:", e.getMessage());
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "users/{id}/invoices/add/2")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @Transactional
    @JsonView(Views.InvoiceCreationSecondStepView.class)
    public ResponseEntity<Invoice> addUsersInvoiceStepTwo(@PathVariable Long id, @RequestBody String invoice) {
        return new ResponseEntity<>(invoiceService.insertValuesToInvoice(invoice), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/invoices/add/gettasks")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    public ResponseEntity<Page<Task>> getTasksFromProject(@PathVariable Long id, @PageableDefault(sort="number") Pageable pageable) {
        return new ResponseEntity<>((userService.tempGetTasksFromProject(pageable)), HttpStatus.OK);
    }

    @PostMapping(value = "users/{id}/invoices/add/addtask")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @Transactional
    @JsonView(Views.InvoiceCreationThirdStepView.class)
    public ResponseEntity<Invoice> attachTasksToInvoice(@PathVariable Long id, @RequestParam(value="invoiceId") Long invoiceId, @RequestBody String tasks) {
        return new ResponseEntity<>(invoiceService.attachTasksToInvoice(invoiceId,tasks,id), HttpStatus.OK);
    }

    @PostMapping(value = "users/{id}/invoices/add/removetask")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @Transactional
    @JsonView(Views.InvoiceCreationThirdStepView.class)
    public ResponseEntity<Invoice> detachTasksFromInvoice(@PathVariable Long id, @RequestParam(value="invoiceId") Long invoiceId, @RequestBody String tasks) {
        return new ResponseEntity<>(invoiceService.detachTasksFromInvoice(invoiceId,tasks,id), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/invoices/add/4")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @Transactional
    @JsonView(Views.InvoiceCreationThirdStepView.class)
    public ResponseEntity<Invoice> sendInvoiceForApproval(@PathVariable Long id, @RequestParam(value="invoiceId") Long invoiceId) {
        return new ResponseEntity<>(invoiceService.sendForApproval(invoiceId), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/invoices/getfile", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @Transactional
    @JsonView(Views.InvoiceCreationThirdStepView.class)
    public ResponseEntity<Void> getInvoiceFile(@PathVariable Long id,@RequestParam(value="fileName") String fileName, HttpServletResponse response) {
        try {
            IOUtils.copy(invoiceService.getInvoiceFile(fileName), response.getOutputStream());
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.flushBuffer();
            return new ResponseEntity<>( HttpStatus.OK);
        }
        catch(IOException e){
            HttpHeaders headers =  new HttpHeaders();
            headers.add("IOException:", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        catch(FileManagerException e){
            HttpHeaders headers =  new HttpHeaders();
            headers.add("FileManagerException:", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "users/{id}/invoices/tasks/estimation")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")

    public ResponseEntity<Page<Task>> getTasksForEstimation(@PathVariable Long id, @PageableDefault(sort="number") Pageable pageable) {
        return new ResponseEntity<>((taskService.getTasksForEstimation(id, pageable)), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/invoices/billdetails")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")

    public ResponseEntity<String> extractBillingDetails(@PathVariable Long id, @RequestParam(value="invoiceId") Long invoiceId) {
        return new ResponseEntity<>(invoiceService.extractBillingDetails(invoiceId), HttpStatus.OK);
    }
}
