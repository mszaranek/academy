package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"projects", "user"})
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
    @JsonView({Views.UserView.class, Views.InvoiceView.class})
    private Double amount;
    @ManyToOne
    //@JsonBackReference
    @JsonView({Views.UserView.class,Views.InvoiceView.class})
    private User user;
    @JsonView({Views.UserView.class,Views.InvoiceView.class})
    private Boolean paid;
    @JsonView({Views.UserView.class,Views.InvoiceView.class})
    private Date date;
    @JsonView({Views.UserView.class,Views.InvoiceView.class})
    private String validationStatus;

    @ManyToMany
    @JoinTable(name = "project_invoice", joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    @JsonBackReference
    private Set<Project> projects = new HashSet<>();

//    @OneToMany
//    private Set<StatusChange> statusChanges = new HashSet<>();

}
