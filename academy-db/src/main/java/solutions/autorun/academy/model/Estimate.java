package solutions.autorun.academy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Task task;
    private Integer value;

    public Estimate(User user, Task task, Integer value){
        this.user=user;
        this.task=task;
        this.value=value;
    }
}
