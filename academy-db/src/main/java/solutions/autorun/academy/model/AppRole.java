package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "users")
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
@JsonIgnoreProperties(ignoreUnknown=true)
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView(Views.UserView.class)
    private String roleName;
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "appRoles")
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    public AppRole() {

    }

    public Long getId() {
        return this.id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String toString() {
        return "AppRole(id=" + this.getId() + ", roleName=" + this.getRoleName() + ", users=" + this.getUsers() + ")";
    }
}
