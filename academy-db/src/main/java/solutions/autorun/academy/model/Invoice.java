package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"projects", "user"})
@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.InvoiceView.class,Views.InvoiceCreationFirstStepView.class})
    private Long id;
    @JsonView({Views.InvoiceView.class, Views.InvoiceCreationSecondStepView.class})
    private String number;
    @JsonView({Views.UserView.class, Views.InvoiceView.class})
    private Double amount;
    @ManyToOne
    @JsonBackReference(value = "user_invoice")
    @JsonView({Views.UserView.class,Views.InvoiceView.class})
    private User user;
    @JsonView({Views.UserView.class,Views.InvoiceView.class})
    private Boolean paid;
    @JsonView({Views.UserView.class,Views.InvoiceView.class})
    private String date;
    @JsonView({Views.UserView.class,Views.InvoiceView.class})
    @Def
    private String validationStatus;

    @JsonView({Views.UserView.class,Views.InvoiceView.class, Views.InvoiceCreationFirstStepView.class})
    private String lifeCycleStatus;

    @JsonView({Views.UserView.class,Views.InvoiceView.class,Views.InvoiceCreationSecondStepView.class})
    private String currency;

    @JsonView({Views.UserView.class,Views.InvoiceView.class, Views.InvoiceCreationSecondStepView.class})
    private Long hours;

    @JsonView({Views.UserView.class,Views.InvoiceView.class,Views.InvoiceCreationSecondStepView.class})
    private Long vat;

    @JsonView({Views.UserView.class,Views.InvoiceView.class, Views.InvoiceCreationSecondStepView.class})
    private String payday;

    @JsonView({Views.InvoiceView.class,Views.InvoiceCreationThirdStepView.class})
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Task> tasks;

    @JsonView({Views.UserView.class,Views.InvoiceView.class, Views.InvoiceCreationFirstStepView.class})
    private String fileName;
    @ManyToMany
    @JoinTable(name = "project_invoice", joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    @JsonBackReference
    private Set<Project> projects = new HashSet<>();

//    @OneToMany
//    private Set<StatusChange> statusChanges = new HashSet<>();

}
