package StudentConsultationSystem.controllers;

import StudentConsultationSystem.components.ErrorPopupComponent;
import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.models.Professor;
import StudentConsultationSystem.models.Student;
import StudentConsultationSystem.repositories.AddAppointmentRepository;
import StudentConsultationSystem.repositories.ProfessorRepository;
import StudentConsultationSystem.repositories.StudentRepository;
import StudentConsultationSystem.utils.SecurityHelper;
import StudentConsultationSystem.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label lblFail;
    @FXML
    private Label lblSuccessful;
    @FXML
    private Label lblEmpty;
    @FXML
    private Label statusLabel;

    private AddAppointmentRepository appointmentDAO;

    private StudentRepository studentDAO;

    @FXML
    private ObservableList<Konsultimet> allAppointments = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void onLoginButtonClick(ActionEvent e) throws Exception {
        String userName = usernameField.getText();
        String password = passwordField.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            lblSuccessful.setVisible(false);
            lblFail.setVisible(false);
            lblEmpty.setVisible(true);
        } else {
            if (userName.equals("admin") && password.equals("admin")) {
                try {

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../views/registerProfessor.fxml"));
                    Parent parent = loader.load();

                    Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Scene scene = new Scene(parent);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (Exception ex) {
                    ErrorPopupComponent.show(ex);
                }
            }else if (userName.startsWith("S")) {
                Student studentUser = loginStudent(userName, password);
                if (studentUser == null) {
                    lblSuccessful.setVisible(false);
                    lblEmpty.setVisible(false);
                    lblFail.setVisible(true);
                } else {
                    lblSuccessful.setVisible(true);
                    lblFail.setVisible(false);
                    lblEmpty.setVisible(false);

                    try {
                        SessionManager.student = studentUser;

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("../views/addAppointment.fxml"));
                        Parent parent = loader.load();


                        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        Scene scene = new Scene(parent);
                        primaryStage.setScene(scene);
                        primaryStage.show();
                        checkForAppointment(e);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }else{
                Professor professorUser = loginProfessor(userName,password);

                if(professorUser == null){
                    lblSuccessful.setVisible(false);
                    lblEmpty.setVisible(false);
                    lblFail.setVisible(true);
                }else{
                    lblSuccessful.setVisible(true);
                    lblFail.setVisible(false);
                    lblEmpty.setVisible(false);

                    try{
                        SessionManager.professor = professorUser;

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("../views/mainScreen.fxml"));
                        Parent parent = loader.load();


                        MainController controller = loader.getController();
                        controller.setView(MainController.CALENDAR_VIEW);

                        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        Scene scene = new Scene(parent);
                        primaryStage.setScene(scene);
                        primaryStage.show();


                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private Professor loginProfessor(String userName, String password) throws Exception {
            Professor professor = ProfessorRepository.find(userName);
            if (professor == null) return professor;

            String hashedPassword = SecurityHelper.computeHash(password, professor.getSalt());
            if (!professor.getPassword().equals(hashedPassword)) return null;

            return professor;

    }
    private Student loginStudent(String userName, String password) throws Exception {
        Student student = StudentRepository.find(userName);
        if (student == null) return student;

        String hashedPassword = SecurityHelper.computeHash(password, student.getSalt());
        if (!student.getPassword().equals(hashedPassword)) return null;

        return student;

    }

    private void checkForAppointment(ActionEvent e){
        try{
            appointmentDAO = new AddAppointmentRepository();
            studentDAO = new StudentRepository();
            allAppointments = AddAppointmentRepository.getAllKonsultimet();

            boolean appointmentFlag  = false;

            LocalTime currentTime = LocalTime.now();
            for(Konsultimet konsultimi : allAppointments){
                LocalTime appointmentStartTime = konsultimi.getFillimi().toLocalTime();
                Long timeDifference = ChronoUnit.MINUTES.between(currentTime, appointmentStartTime);

                if (konsultimi.getFillimi().toLocalDate().equals(LocalDate.now())) {
                    if(timeDifference > -1 && timeDifference <= 15) {
                        appointmentFlag = true;
                        break;
                    }
                }
            }
            if(appointmentFlag) {
                String title = "Appointment Within 15 Minutes";
                Alert logOutConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                logOutConfirmation.setTitle(title);
                logOutConfirmation.setContentText("You have an upcoming appointment within 15 minutes. Do you want to take a look " +
                        "at table consultation?");
                Optional<ButtonType> result = logOutConfirmation.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Parent parent = FXMLLoader.load(getClass().getResource("../views/consultation.fxml"));
                    Scene scene = new Scene(parent);

                    Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
}
