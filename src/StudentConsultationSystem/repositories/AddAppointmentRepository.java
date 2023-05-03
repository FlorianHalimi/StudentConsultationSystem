package StudentConsultationSystem.repositories;

import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.utils.DbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddAppointmentRepository {

    @FXML
    static
    ObservableList<Konsultimet> appointments = FXCollections.observableArrayList();

    @FXML
    static
    ObservableList<PieChart.Data> pieChartData  = FXCollections.observableArrayList();
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
    public static void create(String professor, String student, String email, String lenda, LocalDateTime start, LocalDateTime end, LocalDateTime date) throws Exception{
        Connection conn = DbHelper.getConnection();
        String query = "INSERT INTO konsultimet(Profesori,Studenti,Lenda,fillimi,fundi,dita,email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,professor);
        stmt.setString(2,student);
        stmt.setString(3,lenda);
        stmt.setTimestamp(4,Timestamp.valueOf(start));
        stmt.setTimestamp(5,Timestamp.valueOf(end));
        stmt.setTimestamp(6,Timestamp.valueOf(date));
        stmt.setString(7,email);

        stmt.execute();

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

    public static void DeleteAppointment(int appointmentID) throws Exception {
            String sql = "DELETE FROM konsultimet WHERE Konsultimi_id = ?";
        try {
            Connection conn = DbHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,appointmentID);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("There was an exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void UpdateAppointment(LocalDateTime date, LocalDateTime start, LocalDateTime end,String text,String professor)throws Exception{
        String sql = "UPDATE konsultimet SET date=?, start=?, end=?, text=? WHERE Konsultimi_id = ?";
        try {
            Connection conn = DbHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(date));
            stmt.setTimestamp(2, Timestamp.valueOf(start));
            stmt.setTimestamp(3,Timestamp.valueOf(end));
            stmt.setString(4,text);
            stmt.setString(5,professor);
            stmt.execute();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
