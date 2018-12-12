package solutions.autorun.academy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillingDetails {

    private Long totalEstimatedHours;
    private Long doneEstimatedHours;
    private Long donePercentage;
    private Long bugEstimatedHours;
    private Long bugPercentage;
    private Long invoiceEstimationDifference;

}
