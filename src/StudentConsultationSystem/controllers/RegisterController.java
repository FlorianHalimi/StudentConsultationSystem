package StudentConsultationSystem.controllers;

import StudentConsultationSystem.components.ErrorPopupComponent;
import StudentConsultationSystem.models.Student;
import StudentConsultationSystem.repositories.StudentRepository;
import StudentConsultationSystem.utils.SecurityHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {


    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox gender;
    @FXML
    private TextField emailField;
    @FXML
    private Label lbl;

    ObservableList<String> gjinia = FXCollections.observableArrayList("M", "F");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gender.setItems(gjinia);
    }

    @FXML
    private void onRegisterButtonClick(ActionEvent e) throws Exception {
            if(usernameField.getText().isBlank() == false && passwordField.getText().isBlank() == false && confirmPasswordField.getText().isBlank() == false
            && nameField.getText().isBlank() == false && gender.getSelectionModel().isEmpty() == false && emailField.getText().isBlank() == false){
                if((passwordField.getText().equals(confirmPasswordField.getText())) == false){
                        lbl.setVisible(true);

                } else if (is_Valid_Email(emailField.getText()) == false) {
                        lbl.setVisible(true);
                }else {
                    try {
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        String name = nameField.getText();
                        String email = emailField.getText();

                        Student student = new Student(username, password, name, email);
                        if(register(student) != null){
                            lbl.setVisible(true);
                        }else{
                            ErrorPopupComponent.show("Error");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid credentials!");
                    alert.showAndWait();
                }


            }

    private Student register(Student student) throws Exception {
        String salt = SecurityHelper.generateSalt();
        String hashedPassword = SecurityHelper.computeHash(student.getPassword(), salt);

        student.setPassword(hashedPassword);
        student.setGender(gender.getSelectionModel().getSelectedItem().toString());
        student.setSalt(salt);

        student = StudentRepository.create(student);

        return student;
    }

    public static boolean is_Valid_Email(String email) {
        boolean rez;
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches() == true) {
            rez = true;
        } else
            rez = false;
        return rez;
    }
    @FXML
    private void onBackButtonClick(ActionEvent e) throws Exception{

    }
}
