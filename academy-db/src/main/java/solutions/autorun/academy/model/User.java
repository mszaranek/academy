package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    @JsonView(Views.UserView.class)
    private boolean activated;

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

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public Set<AppRole> getAppRoles() {
        return this.appRoles;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public void setAppRoles(Set<AppRole> appRoles) {
        this.appRoles = appRoles;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ", email=" + this.getEmail() + ", projects=" + this.getProjects() + ", appRoles=" + this.getAppRoles() + ", tasks=" + this.getTasks() + ", invoices=" + this.getInvoices() + ")";
    }

//    @ManyToOne
//    private ProjRole role;

//    public User (Long id){
//        this.id=id;
//    }

}
