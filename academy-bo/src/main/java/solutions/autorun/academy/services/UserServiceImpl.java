package solutions.autorun.academy.services;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.repositories.UserRepository;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements solutions.autorun.academy.services.UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

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
                .on(qInvoice.projects.any().id.as("project_id").eq(qProject.id))
                .join(qInvoice.user, qUser)
                .on(qInvoice.user.id.as("user_id").eq(qUser.id))
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
}

