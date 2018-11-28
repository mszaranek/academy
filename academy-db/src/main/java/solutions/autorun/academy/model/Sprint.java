package solutions.autorun.academy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private Date startDate;
    private Date finishDate;
    @OneToMany
    private Set<Task> tasks = new HashSet<>();

    public Sprint() {

    }
}
