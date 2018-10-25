package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.System;

public interface SystemRepository extends JpaRepository<System, Long> {
}
