package solutions.autorun.academy.services;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.BillingDetails;
import solutions.autorun.academy.model.Invoice;
import solutions.autorun.academy.model.Task;
import solutions.autorun.academy.repositories.InvoiceRepository;
import solutions.autorun.academy.repositories.UserRepository;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final FileManager fileManager;
    private final UserRepository userRepository;

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

    @Override
    public Invoice addInvoice(MultipartFile file, String fileName, Long userId) {

        fileManager.addFile(file,fileName);
        Invoice invoice = new Invoice();
        invoice.setUser(userRepository.findById(userId).get());
        invoice.setFileName(fileName);
        invoice.setLifeCycleStatus("uploaded");
        invoiceRepository.save(invoice);
        return invoice;


    }

    @Override
    public Invoice insertValuesToInvoice(String invoiceString) {
        Gson gson = new GsonBuilder()//
                .disableHtmlEscaping()//
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) //
                .setPrettyPrinting()//
                .serializeNulls()//
                .setDateFormat("yyyy/MM/dd HH:mm:ss [Z]")//
                .create();

        Invoice invoiceInput = gson.fromJson(invoiceString, Invoice.class);

        Invoice invoice = invoiceRepository.findById(invoiceInput.getId()).orElseThrow(() -> new NotFoundException("Invoice not found"));
        invoice.setAmount(invoiceInput.getAmount());
        invoice.setCurrency(invoiceInput.getCurrency());
        invoice.setHours(invoiceInput.getHours());
        invoice.setVat(invoiceInput.getVat());
        invoice.setDate(invoiceInput.getDate());
        invoice.setPayday(invoiceInput.getPayday());
        invoice.setLifeCycleStatus("parsed");
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public Invoice attachTasksToInvoice(Long invoiceId, String tasksString, Long userId){
        Gson gson = new GsonBuilder()//
                .disableHtmlEscaping()//
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) //
                .setPrettyPrinting()//
                .serializeNulls()//
                .setDateFormat("yyyy/MM/dd HH:mm:ss [Z]")//
                .create();
        Set<Task> tasksInput = gson.fromJson(tasksString, Task.class);
        tasksInput.addUsers(userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User Not Found")));
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()-> new NotFoundException("Invoice not found"));
        invoice.getTasks().add(tasksInput);
        invoice.setLifeCycleStatus("paired_with_tasks");
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public Invoice detachTasksFromInvoice(Long invoiceId, String tasksString, Long userId){
        Gson gson = new GsonBuilder()//
                .disableHtmlEscaping()//
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()//
                .serializeNulls()//
                .setDateFormat("yyyy/MM/dd HH:mm:ss [Z]")//
                .create();

        Task tasksInput = gson.fromJson(tasksString, Task.class);
        tasksInput.setUser(null);
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()-> new NotFoundException("Invoice not found"));
        invoice.getTasks().removeIf(task -> task.getNumber().equals(tasksInput.getNumber()));
        invoice.setLifeCycleStatus("paired_with_tasks");
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public Invoice sendForApproval(Long invoiceId){

        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        invoice.setLifeCycleStatus("Sent_for_approval");
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public InputStream getInvoiceFile(String fileName){
        return fileManager.getFile(fileName);
    }

    @Override
    public String extractBillingDetails(Long invoiceId){
        Gson gson = new GsonBuilder()//
                .disableHtmlEscaping()//
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) //
                .setPrettyPrinting()//
                .serializeNulls()//
                .setDateFormat("yyyy/MM/dd HH:mm:ss [Z]")//
                .create();
        BillingDetails billingDetails = new BillingDetails();
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()->new NotFoundException("Invoice not found"));
        Set<Task> tasks = invoice.getTasks();
        billingDetails.setTotalEstimatedHours(tasks.stream().mapToLong(Task::getEstimate).sum());
        billingDetails.setInvoiceEstimationDifference(billingDetails.getTotalEstimatedHours() - invoice.getHours());
        billingDetails.setBugEstimatedHours(tasks.stream().filter(task -> task.getStatus().toLowerCase().equals("bug")).mapToLong(Task::getEstimate).sum());
        billingDetails.setDoneEstimatedHours(tasks.stream().filter(task -> task.getStatus().toLowerCase().equals("done")).mapToLong(Task::getEstimate).sum());
        billingDetails.setBugPercentage((billingDetails.getBugEstimatedHours()/billingDetails.getTotalEstimatedHours()*100));
        billingDetails.setDonePercentage((billingDetails.getDoneEstimatedHours()/billingDetails.getTotalEstimatedHours()*100));

        return gson.toJson(billingDetails);
    }
}
