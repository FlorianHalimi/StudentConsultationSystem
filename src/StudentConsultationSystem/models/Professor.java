package StudentConsultationSystem.models;

public class Professor {

    private int id;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String email;

    private String salt;
    private String phone;
    private String website;

    public Professor(int id, String username, String password, String name, String gender, String email,String salt, String phone, String website){
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.salt = salt;
        this.phone = phone;
        this.website = website;
    }
    public Professor(String name, String username, String email, String phone, String website){
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.website = website;
    }


    public Professor(String username, String password, String name,String email, String phone, String website){
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.website = website;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
