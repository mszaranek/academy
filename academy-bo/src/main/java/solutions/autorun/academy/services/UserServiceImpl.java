package solutions.autorun.academy.services;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.EmailExistsException;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.exceptions.UsernameExistsException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.repositories.UserRepository;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements solutions.autorun.academy.services.UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Set<User> getUsers() {
        return new HashSet<>(userRepository.findAll());
    }

    @Override
    public User registerUser(UserDTO userDTO) throws Exception {
        if(emailExist(userDTO.getEmail())){
            throw new EmailExistsException("e-mail is already in use");
        }
        if(usernameExist(userDTO.getUsername())){
            throw new UsernameExistsException("username is already in use");
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());

        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("User not found")));
    }

    @Override
    public void updateUser(User user) {
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
        HashSet<Tuple> tuples = new HashSet<>(query
                .from(qInvoice)
                .select(qInvoice.id, qInvoice.amount, qInvoice.paid, qInvoice.date, qInvoice.validationStatus)
                .join(qInvoice.projects, qProject)
                .on(qInvoice.projects.any().id.eq(qProject.id))
                .join(qInvoice.user, qUser)
                .on(qInvoice.user.id.eq(qUser.id))
                .where(qInvoice.user.id.eq(userId), (qInvoice.projects.any().id.eq(projectId)))
                .fetch());
        Set<Invoice> invoices = new HashSet<>();

        for (Tuple t : tuples) {
            invoices.add(Invoice.builder()
                    .id(t.get(qInvoice.id))
                    .amount(t.get(qInvoice.amount))
                    .paid(t.get(qInvoice.paid))
                    .date(t.get(qInvoice.date))
                    .validationStatus(t.get(qInvoice.validationStatus))
                    .build());
        }
        return invoices;

    }

    @Override
    @EntityGraph(value="taskEntityGraph")
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
    @EntityGraph(value="taskEntityGraph")
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
    public boolean emailExist(String email){
        if (userRepository.findByEmail(email).isPresent())
        {
            return true;
        }
            return false;
    }

    @Override
    public boolean usernameExist(String username) {
        if (userRepository.findByUsername(username).isPresent()){
            return true;
        }
        return false;
    }
}

