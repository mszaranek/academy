package solutions.autorun.academy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class UserEstimate {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private User user;
    private Integer estimate;
}
