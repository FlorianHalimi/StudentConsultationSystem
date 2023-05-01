package StudentConsultationSystem.repositories;

import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.utils.DbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;
import java.time.LocalDateTime;

public class EditAppointmentRepository {
    @FXML
    static
    ObservableList<Konsultimet> appointments = FXCollections.observableArrayList();

    private static Konsultimet parseRes(ResultSet res) throws SQLException {
        String profesori = res.getString("profesori");
        String studenti = res.getString("studenti");
        String lenda = res.getString("lenda");
        String email = res.getString("email");
        LocalDateTime start = res.getTimestamp("fillimi").toLocalDateTime();
        LocalDateTime end = res.getTimestamp("fundi").toLocalDateTime();
        LocalDateTime creatDate = res.getTimestamp("dita").toLocalDateTime();

        return new Konsultimet(profesori,studenti,lenda,start,end,creatDate,email);
    }
    public static ObservableList<Konsultimet> getAllKonsultimet() throws Exception {

        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM konsultimet");
        ResultSet res = stmt.executeQuery();
        try{
            while(res.next()){
                Konsultimet konsultimet = parseRes(res);
                appointments.add(konsultimet);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return appointments;
    }


    public static void UpdateAppointment(LocalDateTime date, LocalDateTime start, LocalDateTime end,int konsultimiID)throws Exception{
        String sql = "UPDATE konsultimet SET fillimi=?, fundi=?, dita=? WHERE Konsultimi_id = ?";
        try {
            Connection conn = DbHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(date));
            stmt.setTimestamp(2, Timestamp.valueOf(start));
            stmt.setTimestamp(3,Timestamp.valueOf(end));
            stmt.setInt(4,konsultimiID);
            stmt.execute();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
