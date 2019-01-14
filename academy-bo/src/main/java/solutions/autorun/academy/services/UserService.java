package solutions.autorun.academy.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solutions.autorun.academy.model.*;

import java.util.Set;

public interface UserService {

    Set<User> getUsers();

    User createUser(UserDTO userDTO);

    User findUserById(Long id);

    void updateUser(User user);

    void deleteUser(Long id);

    Set<Invoice> findUserInvoicesInProject(Long userId, Long projectId);

    Set<Task> getUsersTasksInProject(Long userId, Long projectId);

    Set<Task> getTaskDetail(Long userId, Long projectId, Long taskId);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    Page<Task> tempGetTasksFromProject(Pageable pageable, Long userId);

    void saveRegisteredUser(User user);

    void addApproleToUser(Long id, Long approleId);

    void addProjroleToUser(Long id, Long projroleId);

    void addTaskToUser(Long id, Long taskId);
}
