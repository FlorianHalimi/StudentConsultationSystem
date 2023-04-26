//package StudentConsultationSystem.repositories;
//
//import StudentConsultationSystem.utils.DbHelper;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class UserRepository {
//
//    private static User parseRes(ResultSet res)throws Exception{
//        int id = res.getInt("id");
//        String username = res.getString("username");
//        String name = res.getString("name");
//        String password = res.getString("password");
//        String salt = res.getString("salt");
//        UserRole role = res.getString("role").equals("S") ? UserRole.STUDENT :UserRole.PROFESSOR;
//        String email = res.getString("email");
//        String phone = res.getString("phone");
//        String website = res.getString("website");
//
//        return new User(id, username,name,password,salt,role,email,phone,website);
//    }
//    public static User create(User model)throws Exception{
//        Connection conn = DbHelper.getConnection();
//        String query = "INSERT INTO users(username, name, password, salt, role, email, phone, website) VALUES(? , ? , ? , ? , ? , ? , ? , ?)";
//        PreparedStatement stmt = conn.prepareStatement(query);
//        stmt.setString(1, model.getUsername());
//        stmt.setString(2, model.getName());
//        stmt.setString(3,model.getPassword());
//        stmt.setString(4, model.getSalt());
//        stmt.setString(5,model.getRole() == UserRole.STUDENT ? "S" : "P");
//        stmt.setString(6, model.getEmail());
//        stmt.setString(7, model.getPhone());
//        stmt.setString(8, model.getWebsite());
//
//
//        if (stmt.executeUpdate() != 1)
//            throw new Exception("ERR_NO_ROW_CHANGE");
//
//        ResultSet res = conn.createStatement().executeQuery("SELECT * FROM users ORDER BY createdAt DESC LIMIT 1");
//        res.next();
//        return parseRes(res);
//    }
//
//    public static int count() throws Exception {
//        Connection conn = DbHelper.getConnection();
//        ResultSet res = conn.createStatement().executeQuery("SELECT COUNT(*) FROM users");
//        res.next();
//        return res.getInt(1);
//    }
//    public static User find(String username) throws Exception {
//        Connection conn = DbHelper.getConnection();
//        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? LIMIT 1");
//        stmt.setString(1, username);
//
//        ResultSet res = stmt.executeQuery();
//        if (!res.next()) return null;
//        return parseRes(res);
//    }
//}
