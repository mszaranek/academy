package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView({Views.UserView.class, Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class})
    private Integer number;
    @ManyToOne
    @JsonView(Views.ProjectsTaskView.class)
    private User user;
    @JsonView({Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class})
    private Integer estimate;
    @JsonView(Views.UsersTaskView.class)
    private Date startDate;
    @JsonView(Views.UsersTaskView.class)
    private Date finishDate;
    @JsonView({Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class})
    private String status;
    @JsonView({Views.UsersTaskView.class,Views.InvoiceCreationThirdStepView.class})
    private String type;
    @ManyToOne
    private Sprint sprint;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    @JsonView({Views.UsersTaskView.class, Views.InvoiceCreationThirdStepView.class})
    private System system;

}
