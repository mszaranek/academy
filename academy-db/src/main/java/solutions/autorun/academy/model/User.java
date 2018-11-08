package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude={"appRoles","projects","tasks","invoices"})
@Entity
@Table(name="\"user\"")
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "username")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    @ManyToMany
    @JoinTable(name = "project_user", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
//    @JsonBackReference
    private Set<Project> projects = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_app_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "app_role_id"))
    @JsonManagedReference
    private Set<AppRole> appRoles = new HashSet<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "user"/*, fetch = FetchType.EAGER*/)
//    @JoinColumn(name="user_id")
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "user"/*, fetch = FetchType.EAGER*/)
//    @JoinColumn(name="user_id")
//    @JsonBackReference
    private Set<Invoice> invoices = new HashSet<>();

//    @ManyToOne
//    private ProjRole role;

}
