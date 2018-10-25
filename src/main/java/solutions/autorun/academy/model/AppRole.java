package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
    @ManyToMany(cascade = {CascadeType.MERGE}, mappedBy = "appRoles")
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    public AppRole(){

    }
}
