package solutions.autorun.academy.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"projects","users"})
@Entity
public class ProjRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
    @OneToMany
    private Set<User> users;

    @ManyToMany
    @JoinTable(name = "project_proj_role", joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "proj_role_id"))
    private Set<Project> projects;

    public ProjRole() {

    }
}
