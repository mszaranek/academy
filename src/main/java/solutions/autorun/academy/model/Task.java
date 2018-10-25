package solutions.autorun.academy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    @ManyToOne
    private User user;
    private Integer estimate;
    private Date startDate;
    private Date finishDate;
    private String status;
    private String type;
    @ManyToOne
    private Sprint sprint;
    @ManyToOne
    private System system;

    public Task(){

    }
}
