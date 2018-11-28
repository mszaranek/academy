package solutions.autorun.academy.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.repositories.InvoiceRepository;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Set<Invoice> getInvoices() {
        return new HashSet<>(invoiceRepository.findAll());
    }

    @Override
    public void createInvoice(Invoice invoice) {
        invoice.setId(null);
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("Invoice not found")));
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long id) {
        invoiceRepository.delete(invoiceRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("Invoice not found"))));
    }
}
