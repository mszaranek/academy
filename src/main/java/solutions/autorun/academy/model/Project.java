package solutions.autorun.academy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public Project(){

    }
}
