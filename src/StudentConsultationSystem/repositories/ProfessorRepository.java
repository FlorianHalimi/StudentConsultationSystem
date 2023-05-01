package StudentConsultationSystem.repositories;

import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.models.Professor;
import StudentConsultationSystem.models.Student;
import StudentConsultationSystem.utils.DbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProfessorRepository {

    private static Professor parseRes(ResultSet res) throws Exception {
        int id = res.getInt("id");
        String username = res.getString("username");
        String password = res.getString("password");
        String name = res.getString("name");
        String gender = res.getString("gender");
        String email = res.getString("email");
        String salt = res.getString("salt");
        String phone = res.getString("phone");
        String website = res.getString("website");

        return new Professor(
                id,username,password, name,gender, email,salt, phone, website
        );
    }
    public ArrayList<Professor> getAllStudent() {
        ArrayList<Professor> list = new ArrayList<>();
        String sql = "SELECT * from professor";
        try {
            Connection conn = DbHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                list.add(parseRes(res));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    public static Professor create(Professor professor) throws Exception {
        Connection conn = DbHelper.getConnection();
        String query = "INSERT INTO professor (username, password,name, gender, email, salt, phone, website) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, professor.getUsername());
        stmt.setString(2, professor.getPassword());
        stmt.setString(3, professor.getName());
        stmt.setString(4, professor.getGender());
        stmt.setString(5, professor.getEmail());
        stmt.setString(6, professor.getSalt());
        stmt.setString(7, professor.getPhone());
        stmt.setString(8, professor.getWebsite());


        if (stmt.executeUpdate() != 1)
            throw new Exception("ERR_NO_ROW_CHANGE");

        ResultSet res = conn.createStatement().executeQuery("SELECT * FROM professor");
        res.next();
        return parseRes(res);
    }
    public static Professor find(String username) throws Exception {
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM professor WHERE username = ? LIMIT 1");
        stmt.setString(1, username);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) return null;
        return parseRes(res);
    }


}
