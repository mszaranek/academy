package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@EqualsAndHashCode(exclude = "system")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "number")
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

}
