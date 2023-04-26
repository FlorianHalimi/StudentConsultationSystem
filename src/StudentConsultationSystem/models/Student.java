package StudentConsultationSystem.models;

public class Student {

    private int id;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String email;

    private String salt;

    public Student(int id, String username, String password, String name, String gender, String email,String salt){
            this.id = id;
            this.username = username;
            this.password = password;
            this.name = name;
            this.gender = gender;
            this.email = email;
            this.salt = salt;
    }

    public Student(String username, String password, String name, String email){
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }
    public Student( String name, String email){
        this.name = name;
        this.email = email;
    }

    public Student() {

    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
