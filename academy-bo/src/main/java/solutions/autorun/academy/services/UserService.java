package solutions.autorun.academy.services;


import com.google.api.client.json.JsonString;
import solutions.autorun.academy.model.*;

import org.springframework.web.multipart.MultipartFile;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.model.User;
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


    Invoice insertValuesToInvoice(String invoiceString);

    Invoice attachTasksToInvoice (Long invoiceId, String tasks);

    Set<Task> tempGetTasksFromProject();


    void saveRegisteredUser(User user);

    Invoice addInvoice(MultipartFile file, String fileName, Long userId);

}
