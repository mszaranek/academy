package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Audited
@Builder
@EqualsAndHashCode(exclude = {"system","users","sprint","estimates"})
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "number")
@NamedEntityGraph(name="taskEntityGraph", attributeNodes={
        @NamedAttributeNode("users"),
        @NamedAttributeNode("sprint"),
        @NamedAttributeNode("system"),
        @NamedAttributeNode("estimates")
})
@JsonIgnoreProperties(ignoreUnknown=true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonView(Views.LogworkView.class)
    private Long id;
    @JsonView({Views.UserView.class, Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class, Views.TaskView.class,Views.LogworkView.class})
    private String number;
    @JsonView({Views.UserView.class, Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class,Views.InvoiceView.class, Views.TaskView.class,Views.LogworkView.class})
    private String summary;
    @ManyToMany
    @JsonView({Views.ProjectsTaskView.class,Views.InvoiceCreationSecondStepView.class})
    @NotAudited
    private Set<User> users;
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
    @JsonIgnore
    private Sprint sprint;
    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JsonView({Views.UsersTaskView.class, Views.InvoiceCreationThirdStepView.class, Views.TaskView.class})
    @NotAudited
    @JsonIgnore
    private System system;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "task")
    @NotAudited
    @JsonView({Views.InvoiceCreationFirstStepView.class, Views.TaskView.class})
    private Set<Estimate> estimates;

    @ManyToOne
    @NotAudited
    @JsonView(Views.LogworkView.class)
    private Project project;

    @NotAudited
    @JsonIgnore
    private String trelloId;

    @Formula("regexp_replace(number,'[^0-9]+','') ::integer")
    @NotAudited
    @JsonView({Views.UserView.class, Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class})
    private Long unsigned;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "task")
    @NotAudited
//    @JsonView(Views.UserView.class)
    @JsonIgnore
    private Set<LogWork> logWorks = new HashSet<>();
    @Formula("regexp_replace(number,'[0-9]+','')")
    @NotAudited
    @JsonView({Views.UserView.class, Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class})
    private String textPart;

}
