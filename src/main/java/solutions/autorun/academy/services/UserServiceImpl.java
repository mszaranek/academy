package solutions.autorun.academy.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.User;
import solutions.autorun.academy.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public Set<User> getUsers() {
        return new HashSet<>(userRepository.findAll());
    }

    @Override
    public void createUser(User user) {
        user.setId(null);
        userRepository.save(user);
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
}
