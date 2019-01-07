package solutions.autorun.academy.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solutions.autorun.academy.Converter.LocalDateConverter;
import solutions.autorun.academy.model.LogWork;
import solutions.autorun.academy.model.LogWorkDTO;
import solutions.autorun.academy.services.LogworkService;
import solutions.autorun.academy.views.Views;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping
public class LogWorkController {

    private final LogworkService logworkService;
    private final LocalDateConverter localDateConverter;

    @GetMapping(value = "users/{id}/logworks")
    @JsonView(Views.LogworkView.class)
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    public ResponseEntity<Set<LogWork>> getUserLogworks(@PathVariable Long id, @RequestParam String date, @RequestParam boolean weekly) {
        return new ResponseEntity<>(logworkService.getUserLogwork(id, localDateConverter.createDate(date), weekly), HttpStatus.OK);
    }


    @GetMapping(value = "users/{id}/logworks/accept")
    @JsonView(Views.LogworkView.class)
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    public ResponseEntity<Set<LogWork>> acceptLogworks(@PathVariable Long id, @RequestParam String date, @RequestParam boolean weekly, @RequestParam Long userId) {
        return new ResponseEntity<>(logworkService.acceptLogworks(id, localDateConverter.createDate(date), weekly, userId), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/logworks/accept/day")
    @JsonView(Views.LogworkView.class)
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    public ResponseEntity<Set<LogWork>> acceptDayLogworks(@PathVariable Long id, @RequestParam String date, @RequestParam Long userId) {
        return new ResponseEntity<>(logworkService.acceptDayLogworks(userId, localDateConverter.createDate(date), userId), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/logworks/validate")
    @JsonView(Views.LogworkView.class)
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    public ResponseEntity<Set<LogWork>> sendToValidation(@PathVariable Long id, @RequestParam String date, @RequestParam boolean weekly) {
        return new ResponseEntity<>(logworkService.sendToValidation(id, localDateConverter.createDate(date), weekly), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/logworks/validate/day")
    @JsonView(Views.LogworkView.class)
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    public ResponseEntity<Set<LogWork>> sendDayToValidation(@PathVariable Long id, @RequestParam String date) {
        return new ResponseEntity<>(logworkService.sendDayToValidation(id, localDateConverter.createDate(date)), HttpStatus.OK);
    }

    @PostMapping(value = "users/{id}/logworks")
    @JsonView(Views.LogworkView.class)
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    public ResponseEntity<LogWork> addLogwork(@PathVariable Long id, @RequestBody LogWorkDTO logWork, @RequestParam Long taskId) {
        return new ResponseEntity<>(logworkService.createLogwork(id, logWork, taskId), HttpStatus.CREATED);
    }

    @PutMapping(value = "users/{id}/logworks")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @JsonView(Views.LogworkView.class)
    public ResponseEntity<LogWork> updateLogwork(@PathVariable Long id, @RequestBody LogWorkDTO logWork,@RequestParam Long logWorkId, @RequestParam Long taskId){
        return new ResponseEntity<>(logworkService.updateLogwork(logWorkId, logWork, taskId), HttpStatus.OK);
    }

    @PutMapping(value = "users/{id}/logworks/correct")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    @JsonView(Views.LogworkView.class)
    public ResponseEntity<LogWork> updateLogworkByManager(@PathVariable Long id, @RequestBody LogWorkDTO logWork,@RequestParam Long logWorkId, @RequestParam Long taskId){
        return new ResponseEntity<>(logworkService.updateLogworkByManager(logWorkId, logWork, taskId), HttpStatus.OK);
    }

    @DeleteMapping(value = "users/{id}/logworks")
    @PreAuthorize("@userRepository.findOneByUsername(authentication.name)==@userRepository.findById(#id)")
    public ResponseEntity<Void> deleteLogwork(@RequestParam Long id) {
        logworkService.deleteLogwork(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
