package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@EqualsAndHashCode(exclude = {"appRoles", "projects", "tasks", "invoices"})
@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "username")
@NamedEntityGraph(name="userEntityGraph", attributeNodes={
        @NamedAttributeNode(value = "projects", subgraph ="userProjectEntityGraph"),
        @NamedAttributeNode(value = "appRoles"),
        @NamedAttributeNode(value = "tasks", subgraph = "userTasksEntityGraph"),
        @NamedAttributeNode(value = "invoices")
},
subgraphs = {
        @NamedSubgraph(name="userProjectEntityGraph", attributeNodes = @NamedAttributeNode("systems")),
        @NamedSubgraph(name="userTasksEntityGraph", attributeNodes = @NamedAttributeNode("sprint"))
                }
        )
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @JsonView(Views.UserView.class)
    private Long id;
    @JsonView({Views.UserView.class,Views.ProjectsTaskView.class,Views.InvoiceView.class})
    private String username;
    private String password;
    @JsonView(Views.UserView.class)
    private String firstName;
    @JsonView(Views.UserView.class)
    private String lastName;
    @JsonView(Views.UserView.class)
    private String email;

    @ManyToMany
    @JsonView(Views.UserView.class)
    @JoinTable(name = "project_user", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
//    @JsonBackReference
    private Set<Project> projects = new HashSet<>();

    @ManyToMany
    @JsonView(Views.UserView.class)
    @JoinTable(name = "user_app_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "app_role_id"))
    @JsonManagedReference
    private Set<AppRole> appRoles = new HashSet<>();

    @JsonView(Views.UserView.class)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "user"/*, fetch = FetchType.EAGER*/)
//    @JoinColumn(name="user_id")
    private Set<Task> tasks = new HashSet<>();

    @JsonView(Views.UserView.class)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "user"/*, fetch = FetchType.EAGER*/)
//    @JoinColumn(name="user_id")
//    @JsonBackReference
    private Set<Invoice> invoices = new HashSet<>();

//    @ManyToOne
//    private ProjRole role;

//    public User (Long id){
//        this.id=id;
//    }

}
