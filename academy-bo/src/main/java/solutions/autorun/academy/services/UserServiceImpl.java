package solutions.autorun.academy.services;

import com.google.api.client.json.JsonString;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.querydsl.jpa.impl.JPAQuery;
import io.jsonwebtoken.lang.Arrays;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.EmailAlreadyUsedException;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.exceptions.UsernameAlreadyUsedException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.repositories.AppRoleRepository;
import solutions.autorun.academy.repositories.InvoiceRepository;
import solutions.autorun.academy.repositories.TaskRepository;
import solutions.autorun.academy.repositories.UserRepository;
import solutions.autorun.academy.repositories.VerificationTokenRepository;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {



    private final AppRoleRepository appRoleRepository;
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final InvoiceRepository invoiceRepository;
    private final TaskRepository taskRepository;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Set<User> getUsers() {
        return new HashSet<>(userRepository.findAll());
    }

    @Override
    public void createVerificationToken(User user, String token) {
    VerificationToken myToken = new VerificationToken();
    myToken.setToken(token);
    myToken.setUser(user);
    tokenRepository.save(myToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public User createUser(UserDTO userDTO) {
        userRepository.findOneByUsername(userDTO.getUsername().toLowerCase())
                .ifPresent(existingUser -> new UsernameAlreadyUsedException("Username is already in use"));
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail())
                .ifPresent(existingUser ->  new EmailAlreadyUsedException("Email is already in use"));
        User user = new User();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setUsername(userDTO.getUsername());
        user.setPassword(encryptedPassword);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setActivated(false);
        Set<AppRole> defaultRoles = new HashSet<>();
        defaultRoles.add(appRoleRepository.findById(2l).get());
        user.setAppRoles(defaultRoles);
        userRepository.save(user);
        return user;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("User not found")));
    }

    @Override
    public void updateUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("User not found"))));
    }

    @Override
    //@EntityGraph(attributePaths = {"tasks", "invoices", "projects", "appRoles"})
    public Set<Invoice> findUserInvoicesInProject(Long userId, Long projectId) {
        JPAQuery<Invoice> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QInvoice qInvoice = QInvoice.invoice;
        QProject qProject = QProject.project;
        return new HashSet<>(query
                .from(qInvoice)
                //.select(qInvoice.id, qInvoice.amount, qInvoice.paid, qInvoice.date, qInvoice.validationStatus)
                .join(qInvoice.projects, qProject)
                .on(qInvoice.projects.any().id.eq(qProject.id))
                .join(qInvoice.user, qUser)
                .on(qInvoice.user.id.eq(qUser.id))
                .where(qInvoice.user.id.eq(userId), (qInvoice.projects.any().id.eq(projectId)))
                .fetch());

    }

    @Override
    @EntityGraph(value = "taskEntityGraph")
    public Set<Task> getUsersTasksInProject(Long userId, Long projectId) {
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QProject qProject = QProject.project;
        QSystem qSystem = QSystem.system;
        QTask qTask = QTask.task;
        QUser qUser = QUser.user;
        return new HashSet<>(query
                .from(qTask)
                //.select(qTask.id, qInvoice.amount, qInvoice.paid, qInvoice.date, qInvoice.validationStatus)
                .join(qTask.system, qSystem)
                .on(qTask.system.id.eq(qSystem.id))
                .join(qSystem.projects, qProject)
                .on(qSystem.projects.any().id.eq(qSystem.id))
                .join(qTask.user, qUser)
                .on(qTask.user.id.eq(qUser.id))
                .where(qUser.id.eq(userId), qProject.id.eq(projectId))
                .fetch());
//        Set<Task> tasks = new HashSet<>();
//
//        for (Tuple t : tuples) {
//            tasks.add(Task.builder()
//                    .id(t.get(qInvoice.id))
//                    .amount(t.get(qInvoice.amount))
//                    .paid(t.get(qInvoice.paid))
//                    .date(t.get(qInvoice.date))
//                    .validationStatus(t.get(qInvoice.validationStatus))
//                    .build());
//        }
//        return tasks;
    }

    @Override
    @EntityGraph(value = "taskEntityGraph")
    public Set<Task> getTaskDetail(Long userId, Long projectId, Long taskId) {
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QProject qProject = QProject.project;
        QSystem qSystem = QSystem.system;
        QTask qTask = QTask.task;
        QUser qUser = QUser.user;
        return new HashSet<>(query
                .from(qTask)
                //.select(qTask.id, qInvoice.amount, qInvoice.paid, qInvoice.date, qInvoice.validationStatus)
                .join(qTask.system, qSystem)
                .on(qTask.system.id.eq(qSystem.id))
                .join(qSystem.projects, qProject)
                .on(qSystem.projects.any().id.eq(qSystem.id))
                .join(qTask.user, qUser)
                .on(qTask.user.id.eq(qUser.id))
                .where(qUser.id.eq(userId), qProject.id.eq(projectId), qTask.id.eq(taskId))
                .fetch());
    }









    @Override
    public Set<Task> tempGetTasksFromProject(){
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QTask qtask = QTask.task;
        return new HashSet<> (query.from(qtask).where(qtask.id.between(4,8)).fetch());
    }



}
