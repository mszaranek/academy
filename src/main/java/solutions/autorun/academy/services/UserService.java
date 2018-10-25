package solutions.autorun.academy.services;

import solutions.autorun.academy.model.User;

import java.util.Set;

public interface UserService {

    Set<User> getUsers();

    void createUser(User user);

    User findUserById(Long id);

    void updateUser(User user);

    void deleteUser(Long id);
}
