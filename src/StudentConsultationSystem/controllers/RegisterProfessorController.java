package StudentConsultationSystem.controllers;

import StudentConsultationSystem.models.Professor;
import StudentConsultationSystem.models.Student;
import StudentConsultationSystem.repositories.ProfessorRepository;
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

public class RegisterProfessorController implements Initializable {
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
    private TextField phoneField;
    @FXML
    private TextField websiteField;
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
                    String phone = phoneField.getText();
                    String website = websiteField.getText();

                    Professor professor = new Professor(username, password, name, email,phone,website);
                    if(register(professor) != null){
                        lbl.setVisible(true);
                    }else{
                        // shfaq pop up error
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

    private Professor register(Professor professor) throws Exception {
        String salt = SecurityHelper.generateSalt();
        String hashedPassword = SecurityHelper.computeHash(professor.getPassword(), salt);

        professor.setPassword(hashedPassword);
        professor.setGender(gender.getSelectionModel().getSelectedItem().toString());
        professor.setSalt(salt);

        professor = ProfessorRepository.create(professor);

        return professor;
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
