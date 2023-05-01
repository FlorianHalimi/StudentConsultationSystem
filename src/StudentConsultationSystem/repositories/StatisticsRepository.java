package StudentConsultationSystem.repositories;

import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.models.Professor;
import StudentConsultationSystem.utils.DbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StatisticsRepository {

    static ObservableList<String> data = FXCollections.observableArrayList();
    public static ObservableList<String> getLenda(String professor) throws Exception {
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT Lenda FROM konsultimet WHERE Profesori = ?");
        stmt.setString(1, professor);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            String lenda = res.getString("Lenda");
            data.add(lenda);
        }
        return data;
    }
}
