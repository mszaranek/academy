package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"users", "systems", "invoices", "projRoles"})
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView(Views.UserView.class)
    private String name;
    private Double monthlyCost;
    private String status;
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "projects")
    private Set<User> users = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "projects")
    private Set<System> systems = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "projects")
    private Set<Invoice> invoices = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "projects")
    private Set<ProjRole> projRoles = new HashSet<>();

    public Project() {

    }
}
