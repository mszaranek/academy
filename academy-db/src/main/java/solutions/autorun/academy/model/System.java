package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"projects","tasks"})
@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
@JsonIgnoreProperties(ignoreUnknown=true)
public class System {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView({Views.UsersTaskView.class, Views.InvoiceCreationThirdStepView.class, Views.TaskView.class})
    private String name;
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "system")
    @JsonIgnore
    private Set<Task> tasks = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "project_system", joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "system_id"))
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    public System() {

    }

}
