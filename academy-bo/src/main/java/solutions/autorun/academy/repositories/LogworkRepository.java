package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.LogWork;

@Repository
public interface LogworkRepository extends JpaRepository<LogWork, Long> {
}
