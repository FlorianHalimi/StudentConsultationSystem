package StudentConsultationSystem.controllers;


import StudentConsultationSystem.repositories.ProfessorRepository;
import StudentConsultationSystem.utils.SecurityHelper;
import StudentConsultationSystem.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordController extends ChildController{

    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmNewPassword;
    @FXML
    private Button updateButton;
    private String currentPSW;
    private String newPSW;
    private String confirmNewPSW;
    private String professorSalt = SessionManager.professor.getSalt();
    private String professorUsername = SessionManager.professor.getUsername();
    private String professorPassword = SessionManager.professor.getPassword();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void gatherInformation(){
        currentPSW = currentPassword.getText();
        newPSW = newPassword.getText();
        confirmNewPSW = confirmNewPassword.getText();
    }
    private boolean validatePassword()throws Exception{
        gatherInformation();
        String hashedPassword = SecurityHelper.computeHash(currentPSW,professorSalt);
        if(currentPSW.isBlank() || newPSW.isBlank() || confirmNewPSW.isBlank()) {
            Alert missingInformation = new Alert(Alert.AlertType.ERROR);
            missingInformation.setTitle("Mungese te informatave");
            missingInformation.setContentText("Ju lutem, plotesoni te gjitha fushat");
            DialogPane dialogPane = missingInformation.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("../resources/styles/style.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
            missingInformation.showAndWait();
            return false;
        }else if(!hashedPassword.equals(professorPassword)){
            Alert missingInformation = new Alert(Alert.AlertType.ERROR);
            missingInformation.setTitle("Gabim");
            missingInformation.setContentText("Passwordi juaj nuk eshte i sakte!");
            DialogPane dialogPane = missingInformation.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("../resources/styles/style.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
            missingInformation.showAndWait();
            return false;
        }else if(!newPSW.equals(confirmNewPSW)){
            Alert missingInformation = new Alert(Alert.AlertType.ERROR);
            missingInformation.setTitle("Gabim");
            missingInformation.setContentText("Passwordi i ri nuk perputhet. Ju lutem shkruani perseri!");
            DialogPane dialogPane = missingInformation.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("../resources/styles/style.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
            missingInformation.showAndWait();
            return false;
        }else if(!validatePassword(newPSW)){
            Alert missingInformation = new Alert(Alert.AlertType.ERROR);
            missingInformation.setTitle("Gabim");
            missingInformation.setContentText("Password duhet te kete te pakten 8 karaktere, duhet te jete kombinim i shkronjave, numrave dhe karaktereve speciale(!$@%).");
            missingInformation.setResizable(true);
            missingInformation.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            DialogPane dialogPane = missingInformation.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("../resources/styles/style.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
            missingInformation.showAndWait();
            return false;
        }
        return true;
    }
    private String hashedPassword() throws Exception {
        gatherInformation();
        String hashedPassword = SecurityHelper.computeHash(currentPSW,professorSalt);
        return hashedPassword;
    }

    @FXML
    public void onUpdateButtonClick(ActionEvent e)throws  Exception{
        try{
            if(validatePassword()){
                String hashedNewPassword = SecurityHelper.computeHash(newPSW,professorSalt);
                ProfessorRepository.UpdatePassword(hashedNewPassword,professorUsername);
                Alert updatePasswordInformation = new Alert(Alert.AlertType.INFORMATION);
                updatePasswordInformation.setTitle("Njoftim");
                updatePasswordInformation.setContentText("Passwordi u ndryshua me sukses!");
                DialogPane dialogPane = updatePasswordInformation.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("../resources/styles/style.css").toExternalForm());
                dialogPane.getStyleClass().add("alert");
                updatePasswordInformation.showAndWait();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean validatePassword(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!$@%])[a-zA-Z0-9!$@%]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
