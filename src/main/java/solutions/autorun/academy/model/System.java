package solutions.autorun.academy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class System {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "system")
    private Set<Task> tasks = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "project_system", joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "system_id"))
    private Set<Project> projects = new HashSet<>();

    public System(){

    }

}
