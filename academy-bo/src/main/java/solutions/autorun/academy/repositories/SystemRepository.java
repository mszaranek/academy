package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.System;

@Repository
public interface SystemRepository extends JpaRepository<System, Long> {
}
