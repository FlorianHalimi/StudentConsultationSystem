package StudentConsultationSystem.repositories;

import StudentConsultationSystem.models.Student;
import StudentConsultationSystem.utils.DbHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentRepository {

    private static Student parseRes(ResultSet res) throws Exception {
        int id = res.getInt("id");
        String username = res.getString("username");
        String password = res.getString("password");
        String name = res.getString("name");
        String gender = res.getString("gender");
        String email = res.getString("email");
        String salt = res.getString("salt");

        return new Student(
                id,username,password, name,gender, email,salt
        );
    }
    public List<Student> getAllStudent() {
        ArrayList<Student> list = new ArrayList<>();
        String sql = "SELECT * from student";
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
    public static Student create(Student student) throws Exception {
        Connection conn = DbHelper.getConnection();
        String query = "INSERT INTO student (username, password,name, gender, email, salt) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, student.getUsername());
        stmt.setString(2, student.getPassword());
        stmt.setString(3, student.getName());
        stmt.setString(4, student.getGender());
        stmt.setString(5, student.getEmail());
        stmt.setString(6, student.getSalt());

        if (stmt.executeUpdate() != 1)
                throw new Exception("ERR_NO_ROW_CHANGE");

        ResultSet res = conn.createStatement().executeQuery("SELECT * FROM student");
        res.next();
        return parseRes(res);
    }
    public static Student find(int id) throws Exception {
            Connection conn = DbHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM student WHERE id = ? LIMIT 1");
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();
            if (!res.next()) return null;
            return parseRes(res);
    }

    public static Student find(String username) throws Exception {
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM student WHERE username = ? LIMIT 1");
        stmt.setString(1, username);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) return null;
        return parseRes(res);
    }

    public static String getEmail(String name) throws Exception{
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT email FROM student WHERE name = ?");
        stmt.setString(1, name);

        ResultSet res = stmt.executeQuery();
        res.next();
        return res.getString(1);
    }
}



