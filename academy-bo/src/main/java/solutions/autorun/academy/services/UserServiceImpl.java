package solutions.autorun.academy.services;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.EmailAlreadyUsedException;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.exceptions.UsernameAlreadyUsedException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.repositories.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final ProjRoleRepository projRoleRepository;
    private final AppRoleRepository appRoleRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
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
    public Set<Invoice> findUserInvoicesInProject(Long userId, Long projectId) {
        JPAQuery<Invoice> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QInvoice qInvoice = QInvoice.invoice;
        QProject qProject = QProject.project;
        return new HashSet<>(query
                .from(qInvoice)
                .join(qInvoice.projects, qProject)
                .on(qInvoice.projects.any().id.eq(qProject.id))
                .join(qInvoice.user, qUser)
                .on(qInvoice.user.id.eq(qUser.id))
                .where(qInvoice.user.id.eq(userId), (qInvoice.projects.any().id.eq(projectId)))
                .fetch());

    }

    @Override
  //  @EntityGraph(value = "taskEntityGraph")
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
                .join(qTask.users, qUser)
                .on(qTask.users.any().id.eq(qUser.id))
                .where(qUser.id.eq(userId), qProject.id.eq(projectId))
                .fetch());
    }

    @Override
   // @EntityGraph(value = "taskEntityGraph")
    public Set<Task> getTaskDetail(Long userId, Long projectId, Long taskId) {
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QProject qProject = QProject.project;
        QSystem qSystem = QSystem.system;
        QTask qTask = QTask.task;
        QUser qUser = QUser.user;
        return new HashSet<>(query
                .from(qTask)
                .join(qTask.system, qSystem)
                .on(qTask.system.id.eq(qSystem.id))
                .join(qSystem.projects, qProject)
                .on(qSystem.projects.any().id.eq(qSystem.id))
                .join(qTask.users, qUser)
                .on(qTask.users.any().id.eq(qUser.id))
                .where(qUser.id.eq(userId), qProject.id.eq(projectId), qTask.id.eq(taskId))
                .fetch());
    }

    @Override
    public Page<Task> tempGetTasksFromProject(Pageable pageable, Long userId){

        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QTask qTask = QTask.task;
        List<Task> tasks = new ArrayList<>(query.from(qTask)
                .orderBy(qTask.textPart.asc(),qTask.unsigned.asc()).fetch());
        Predicate<Task> con1 = task -> task.getUsers().isEmpty();
        Predicate<Task> con2 = task -> task.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
        tasks = tasks.stream().filter(con1.or(con2)).collect(Collectors.toList());
        //log.debug(tasks.toString());
        int start = (int) pageable.getOffset();
        int end =  (start + pageable.getPageSize()) > tasks.size() ? tasks.size() : (start + pageable.getPageSize());
        Page<Task> page = new PageImpl<>(tasks.subList(start,end), pageable, tasks.size());

        return page;
    }

    @Override
    public void addApproleToUser(Long id, Long approleId) {
        User user = userRepository.findById(id).get();
        user.getAppRoles().add(appRoleRepository.findById(approleId).get());
        userRepository.save(user);
    }

    @Override
    public void addProjroleToUser(Long id, Long projroleId) {
        User user = userRepository.findById(id).get();
        user.setProjRole(projRoleRepository.findById(projroleId).get());
        userRepository.save(user);
    }

    @Override
    public void addTaskToUser(Long id, Long taskId) {
        User user = userRepository.findById(id).get();
        user.getTasks().add(taskRepository.findById(taskId).get());
        userRepository.save(user);
    }
}
