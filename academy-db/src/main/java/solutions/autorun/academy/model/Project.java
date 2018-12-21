package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"users", "systems", "invoices", "projRoles", "tasks"})
@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "name")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonView(Views.LogworkView.class)
    private Long id;
    @JsonView({Views.UserView.class,Views.UsersProjectsView.class,Views.LogworkView.class})
    private String name;
    @JsonIgnore
    private Double monthlyCost;
    @JsonIgnore
    private String status;

    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "projects")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "projects")
    @JsonIgnore
    private Set<System> systems = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "projects")
    @JsonIgnore
    private Set<Invoice> invoices = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "projects")
    @JsonIgnore
    private Set<ProjRole> projRoles = new HashSet<>();
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "project")
    @JsonView(Views.ProjectsTaskView.class)
    private Set<Task> tasks = new HashSet<>();




    public Project() {

    }
}
