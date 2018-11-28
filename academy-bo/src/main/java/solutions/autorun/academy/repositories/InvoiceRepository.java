package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.autorun.academy.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
