package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @ManyToOne
    //@JsonView(Views.InvoiceCreationFirstStepView.class)
    @JsonIgnore
    private User user;
    @ManyToOne
    @JsonIgnore
    private Task task;
    @JsonView({Views.InvoiceCreationFirstStepView.class, Views.EstimateValueView.class})

    private Integer value;

    public Estimate(User user, Task task, Integer value){
        this.user=user;
        this.task=task;
        this.value=value;
    }
}
