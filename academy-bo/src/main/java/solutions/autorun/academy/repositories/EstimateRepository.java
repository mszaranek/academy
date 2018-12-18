package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.Estimate;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.model.User;

import java.util.Optional;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    Optional<Estimate> findByUserAndTask(User user, Task task);

}
