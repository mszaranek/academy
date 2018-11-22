package solutions.autorun.academy.services;

import solutions.autorun.academy.exceptions.EmailExistsException;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.model.UserDTO;

import java.util.Set;

public interface UserService {

    Set<User> getUsers();

    User registerUser(UserDTO userDTO) throws Exception;

    User findUserById(Long id);

    void updateUser(User user);

    void deleteUser(Long id);

    Set<Invoice> findUserInvoicesInProject(Long userId, Long projectId);

    Set<Task> getUsersTasksInProject(Long userId, Long projectId);

    Set<Task> getTaskDetail(Long userId, Long projectId, Long taskId);

    boolean emailExist(String email);

    boolean usernameExist(String username);
}
