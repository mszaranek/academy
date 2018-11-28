package solutions.autorun.academy.services;

import com.google.api.client.json.JsonString;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.querydsl.jpa.impl.JPAQuery;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;
import solutions.autorun.academy.exceptions.NotFoundException;
import solutions.autorun.academy.model.*;
import solutions.autorun.academy.repositories.InvoiceRepository;
import solutions.autorun.academy.repositories.TaskRepository;
import solutions.autorun.academy.repositories.UserRepository;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Value("${solutions.autorun.academy.minio.endpoint}")
    String minioEndpoint;
    @Value("${solutions.autorun.academy.minio.accessKey}")
    String minioAccessKey;
    @Value("${solutions.autorun.academy.minio.secretKey}")
    String minioSecretKey;
    @Value("${solutions.autorun.academy.minio.bucket}")
    String minioBucket;

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final TaskRepository taskRepository;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Set<User> getUsers() {
        return new HashSet<>(userRepository.findAll());
    }

    @Override
    public void createUser(User user) {
        user.setId(null);
        userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("User not found")));
    }

    @Override
    public void updateUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow((() -> new NotFoundException("User not found"))));
    }

    @Override
    //@EntityGraph(attributePaths = {"tasks", "invoices", "projects", "appRoles"})
    public Set<Invoice> findUserInvoicesInProject(Long userId, Long projectId) {
        JPAQuery<Invoice> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QInvoice qInvoice = QInvoice.invoice;
        QProject qProject = QProject.project;
        return new HashSet<>(query
                .from(qInvoice)
                //.select(qInvoice.id, qInvoice.amount, qInvoice.paid, qInvoice.date, qInvoice.validationStatus)
                .join(qInvoice.projects, qProject)
                .on(qInvoice.projects.any().id.eq(qProject.id))
                .join(qInvoice.user, qUser)
                .on(qInvoice.user.id.eq(qUser.id))
                .where(qInvoice.user.id.eq(userId), (qInvoice.projects.any().id.eq(projectId)))
                .fetch());

    }

    @Override
    @EntityGraph(value = "taskEntityGraph")
    public Set<Task> getUsersTasksInProject(Long userId, Long projectId) {
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QProject qProject = QProject.project;
        QSystem qSystem = QSystem.system;
        QTask qTask = QTask.task;
        QUser qUser = QUser.user;
        return new HashSet<>(query
                .from(qTask)
                //.select(qTask.id, qInvoice.amount, qInvoice.paid, qInvoice.date, qInvoice.validationStatus)
                .join(qTask.system, qSystem)
                .on(qTask.system.id.eq(qSystem.id))
                .join(qSystem.projects, qProject)
                .on(qSystem.projects.any().id.eq(qSystem.id))
                .join(qTask.user, qUser)
                .on(qTask.user.id.eq(qUser.id))
                .where(qUser.id.eq(userId), qProject.id.eq(projectId))
                .fetch());
//        Set<Task> tasks = new HashSet<>();
//
//        for (Tuple t : tuples) {
//            tasks.add(Task.builder()
//                    .id(t.get(qInvoice.id))
//                    .amount(t.get(qInvoice.amount))
//                    .paid(t.get(qInvoice.paid))
//                    .date(t.get(qInvoice.date))
//                    .validationStatus(t.get(qInvoice.validationStatus))
//                    .build());
//        }
//        return tasks;
    }

    @Override
    @EntityGraph(value = "taskEntityGraph")
    public Set<Task> getTaskDetail(Long userId, Long projectId, Long taskId) {
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QProject qProject = QProject.project;
        QSystem qSystem = QSystem.system;
        QTask qTask = QTask.task;
        QUser qUser = QUser.user;
        return new HashSet<>(query
                .from(qTask)
                //.select(qTask.id, qInvoice.amount, qInvoice.paid, qInvoice.date, qInvoice.validationStatus)
                .join(qTask.system, qSystem)
                .on(qTask.system.id.eq(qSystem.id))
                .join(qSystem.projects, qProject)
                .on(qSystem.projects.any().id.eq(qSystem.id))
                .join(qTask.user, qUser)
                .on(qTask.user.id.eq(qUser.id))
                .where(qUser.id.eq(userId), qProject.id.eq(projectId), qTask.id.eq(taskId))
                .fetch());
    }

    @Override
    public Invoice addInvoice(MultipartFile file, String fileName, Long userId) {
        try {
            MinioClient minioClient = new MinioClient(minioEndpoint, minioAccessKey,
                    minioSecretKey);
            boolean found = minioClient.bucketExists(minioBucket);
            if (found) {
                System.out.println(minioBucket + " already exists");
            } else {
                minioClient.makeBucket(minioBucket);
                System.out.println(minioBucket + " is created successfully");
            }
            byte[] byteArr = file.getBytes();
            InputStream bais = new ByteArrayInputStream(byteArr);
            minioClient.putObject(minioBucket, fileName, bais, bais.available(), "application/octet-stream");
            bais.close();
            System.out.println(fileName + " is uploaded successfully");
            Invoice invoice = new Invoice();
            invoice.setUser(userRepository.findById(userId).get());
            invoice.setFileName(fileName);
            invoice.setLifeCycleStatus("uploaded");
            invoiceRepository.save(invoice);
            return invoice;

        } catch (
                MinioException e) {
            System.out.println("Error occurred: " + e);
            return null;
        } catch (
                IOException e) {
            System.out.println("Error occurred: " + e);
            return null;
        } catch (
                NoSuchAlgorithmException e) {
            System.out.println("Error occurred: " + e);
            return null;
        } catch (
                InvalidKeyException e) {
            System.out.println("Error occurred: " + e);
            return null;
        } catch (
                XmlPullParserException e) {
            System.out.println("Error occurred: " + e);
            return null;
        }
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
    public Invoice attachTasksToInvoice(Long invoiceId, String tasksString){
        Gson gson = new GsonBuilder()//
                .disableHtmlEscaping()//
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) //
                .setPrettyPrinting()//
                .serializeNulls()//
                .setDateFormat("yyyy/MM/dd HH:mm:ss [Z]")//
                .create();
        Type founderSetType = new TypeToken<HashSet<Task>>(){}.getType();
        Set<Task> tasksInput = gson.fromJson(tasksString, founderSetType);
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()-> new NotFoundException("Invoice not found"));
        invoice.setTasks(tasksInput);
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public Set<Task> tempGetTasksFromProject(){
        JPAQuery<Task> query = new JPAQuery<>(entityManager);
        QTask qtask = QTask.task;
        return new HashSet<> (query.from(qtask).where(qtask.id.between(4,8)).fetch());
    }

}
