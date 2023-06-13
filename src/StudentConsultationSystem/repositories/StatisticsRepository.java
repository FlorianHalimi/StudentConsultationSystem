package StudentConsultationSystem.repositories;

import StudentConsultationSystem.utils.DbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StatisticsRepository {

    static ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
    static ObservableList<LocalDate> timeList = FXCollections.observableArrayList();
    public static ObservableList<PieChart.Data> getLenda(String professor) throws Exception {
        data.clear();
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT Lenda, COUNT(*) FROM konsultimet WHERE Profesori = ?  " +
                                                            "and DATE(fillimi) > CURDATE() GROUP BY Lenda");
        stmt.setString(1, professor);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            String lenda = res.getString(1);
            int count = res.getInt(2);
            data.add(new PieChart.Data(lenda,count));
        }
        return data;
    }
    public static ObservableList<LocalDate> getTimeList(String professor) throws Exception {
        timeList.clear();
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT fillimi FROM konsultimet WHERE Profesori = ? " +
                                                            "and DATE(fillimi) > CURDATE();");
        stmt.setString(1, professor);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            LocalDateTime dateTime = res.getTimestamp("fillimi").toLocalDateTime();
            LocalDate date = dateTime.toLocalDate();
            timeList.add(date);
        }
        return timeList;
    }

}
