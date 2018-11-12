package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude={"projects","user"})
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinTable(name = "project_invoice", joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    @JsonBackReference
    private Set<Project> projects = new HashSet<>();

//    @OneToMany
//    private Set<StatusChange> statusChanges = new HashSet<>();

}
