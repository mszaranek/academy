package solutions.autorun.academy.Converter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Service
public class LocalDateConverter {

    public LocalDateTime createDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime localDate = LocalDateTime.parse(dateString, formatter);
        System.out.println(localDate);
        return localDate;
    }

    public LocalDate createDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        System.out.println(localDate);
        return localDate;
    }
}
