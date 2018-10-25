package solutions.autorun.academy.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude="projects")
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    @ManyToOne
    private User user;
    private Boolean paid;
    private Date date;
    private String validationStatus;

    @ManyToMany
    @JoinTable(name = "project_invoice", joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "invoice_id"))
    private Set<Project> projects = new HashSet<>();

    @OneToMany
    private Set<StatusChange> statusChanges = new HashSet<>();

    public Invoice(){

    }
}
