package solutions.autorun.academy.model;

import java.time.LocalDate;

public class LogWorkDTO {

    private String date;
    private String description;
    private Long workedTime;
    private String status;
    private String verifyMethodUsed;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWorkedTime() {
        return workedTime;
    }

    public void setWorkedTime(Long workedTime) {
        this.workedTime = workedTime;
    }
}