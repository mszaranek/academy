package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Audited
@Builder
@EqualsAndHashCode(exclude = {"system","user","sprint"})
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "number")
@NamedEntityGraph(name="taskEntityGraph", attributeNodes={
        @NamedAttributeNode("user"),
        @NamedAttributeNode("sprint"),
        @NamedAttributeNode("system")
})
@JsonIgnoreProperties(ignoreUnknown=true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView({Views.UserView.class, Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class, Views.TaskView.class})
    private String number;
    @JsonView({Views.UserView.class, Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class, Views.TaskView.class})
    private String summary;
    @ManyToOne
    @JsonView(Views.ProjectsTaskView.class)
    @NotAudited
    private User user;
    @JsonView({Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class, Views.TaskView.class})
    private Integer estimate;
    @JsonView(Views.UsersTaskView.class)
    private Date startDate;
    @JsonView(Views.UsersTaskView.class)
    private Date finishDate;
    @JsonView({Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class, Views.TaskView.class})
    private String dueDate;

    @JsonView({Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class})
    private String status;
    @JsonView({Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class})
    private String type;
    @ManyToOne
    @NotAudited
    private Sprint sprint;
    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JsonView({Views.UsersTaskView.class, Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class, Views.TaskView.class})
    @NotAudited
    @JsonIgnore
    private System system;

    @NotAudited
    private String trelloId;

    @Formula("regexp_replace(number,'[^0-9]+','') ::integer")
    @NotAudited
    @JsonView({Views.UserView.class, Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class})
    private Long unsigned;

}
