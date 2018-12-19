package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class StatusChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Invoice invoice;
    private String statusBefore;
    private String statusAfter;
    private Date date;
    @ManyToOne
    private User creator;
    private String reason;

    public StatusChange() {

    }
}
