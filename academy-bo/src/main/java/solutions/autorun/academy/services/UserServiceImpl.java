package solutions.autorun.academy.services;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.EmailAlreadyUsedException;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.exceptions.UsernameAlreadyUsedException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.repositories.UserRepository;
import solutions.autorun.academy.repositories.VerificationTokenRepository;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements solutions.autorun.academy.services.UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
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
}



