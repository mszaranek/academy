package solutions.autorun.academy.controllers;

import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.services.UserService;
import solutions.autorun.academy.services.InvoiceService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;
    private final InvoiceService invoiceService;

    @GetMapping(value = "/users")
    public ResponseEntity<Set<User>> showUsers() {
        long startTime = System.currentTimeMillis();
        Set<User> users = userService.getUsers();
        long estimatedTime = (System.currentTimeMillis() - startTime)/1000;
        System.out.println("Time: "+estimatedTime);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "user/{id}")
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
    public ResponseEntity<Set<Invoice>> showUsersInvoices(@PathVariable Long id) {

        return new ResponseEntity<>(userService.findUserById(id).getInvoices(), HttpStatus.OK);
    }
    @GetMapping(value = "user/{userId}/projects/{projectId}/invoices")
    public ResponseEntity<Set<Invoice>> showUsersInvoicesInProject(@PathVariable Long userId, @PathVariable Long projectId) {

        return new ResponseEntity<>(userService.findUserInvoicesInProject(userId,projectId), HttpStatus.OK);
    }
}
