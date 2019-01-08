package solutions.autorun.academy.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import solutions.autorun.academy.views.Views;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@EqualsAndHashCode(exclude = {"user","task"})
@NamedEntityGraph(name="logWorksEntityGraph", attributeNodes={
        @NamedAttributeNode("user"),
        @NamedAttributeNode("task")
})
public class LogWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.UserView.class, Views.LogworkView.class, Views.LogworkViewInProject.class})
    private Long id;
    @JsonView({Views.UserView.class, Views.LogworkView.class, Views.LogworkViewInProject.class})
    private String status;
    @JsonView({Views.UserView.class, Views.LogworkView.class, Views.LogworkViewInProject.class})
    private String verifyMethodUsed;
    @JsonView({Views.UserView.class, Views.LogworkView.class, Views.LogworkViewInProject.class})
    private LocalDate date;
    @JsonView({Views.UserView.class, Views.LogworkView.class, Views.LogworkViewInProject.class})
    private String description;
    @JsonView({Views.UserView.class, Views.LogworkView.class, Views.LogworkViewInProject.class})
    private Long workedTime;
    @JsonView({Views.UserView.class, Views.LogworkView.class, Views.LogworkViewInProject.class})
    @ManyToOne
    private Task task;
    @ManyToOne
    @JsonBackReference(value = "user_worklog")
    @JsonView({Views.LogworkView.class, Views.LogworkViewInProject.class})
    private User user;
}
