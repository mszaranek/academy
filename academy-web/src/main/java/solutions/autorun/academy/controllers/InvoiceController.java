package solutions.autorun.academy.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.services.InvoiceService;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<Set<Invoice>> showInvoices() {
        long startTime = System.currentTimeMillis();
        Set<Invoice> invoices = invoiceService.getInvoices();
        long estimatedTime = (System.currentTimeMillis() - startTime)/1000;
        System.out.println("Time: "+estimatedTime);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createInvoice(@RequestBody Invoice invoice) {
        invoiceService.createInvoice(invoice);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Invoice> findInvoice(@PathVariable Long id) {
        return new ResponseEntity<>(invoiceService.findInvoiceById(id), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> updateInvoice(@RequestBody Invoice invoice) {
        invoiceService.findInvoiceById(invoice.getId());
        invoiceService.updateInvoice(invoice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
