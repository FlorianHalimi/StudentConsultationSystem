package StudentConsultationSystem.repositories;

import javafx.scene.control.ComboBox;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class TimeConversion {
    public static ComboBox<LocalTime> populateTimes(ComboBox<LocalTime> localTimeComboBox, LocalTime startTime, LocalTime endTime) {
        while(startTime.isBefore(endTime.plusSeconds(1))) {
            localTimeComboBox.getItems().add(startTime);
            startTime = startTime.plusMinutes(15);
        }
        return localTimeComboBox;
    }

    public static String formatDate(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return formatter.format(localDateTime);

    }
    public static String formatTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return formatter.format(localDateTime);
    }

}
