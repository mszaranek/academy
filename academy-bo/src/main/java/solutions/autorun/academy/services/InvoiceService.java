package solutions.autorun.academy.services;

import solutions.autorun.academy.model.Invoice;

import java.util.Set;

public interface InvoiceService {
    Set<Invoice> getInvoices();

    void createInvoice(Invoice invoice);

    Invoice findInvoiceById(Long id);

    void updateInvoice(Invoice invoice);

    void deleteInvoice(Long id);
}
