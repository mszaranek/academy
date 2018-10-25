package solutions.autorun.academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.autorun.academy.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
