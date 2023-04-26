package StudentConsultationSystem.controllers;

import StudentConsultationSystem.components.ErrorPopupComponent;
import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.models.Student;
import StudentConsultationSystem.repositories.AddAppointmentRepository;
import StudentConsultationSystem.repositories.StudentRepository;
import StudentConsultationSystem.repositories.TimeConversion;
import StudentConsultationSystem.utils.DbHelper;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddApointmentController implements Initializable {
    private final String[][] subItems = {
            {"Sisteme te shperndara"},
            {"Siguria e te dhenave", "Rrjetet Kompjuterike", "Siguria e Internetit", "Inxhinieria Softuerike"},
            {"Arkitektura e kompjuterve", "Programimi ne internet"},
            {"Gjuhe programuese", "Algoritmet dhe strukturat e te dhenave","Praktika Profesionale"}
    };
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> profesoret;
    @FXML
    private ComboBox<String> lendet;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label lblEmpty;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    @FXML
    private VBox contentPane;
    private ObservableList<Konsultimet> appointments = FXCollections.observableArrayList();

    private LocalTime start = LocalTime.of(9,0);
    private LocalTime end = LocalTime.of(11, 0);
    private String name;
    private String email;
    private String professor;
    private String lenda;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private String studentName = SessionManager.student.getName();
    private String studentEmail = SessionManager.student.getEmail();

    ObservableList<String> mainItems = FXCollections.observableArrayList("Dhurate Hyseni", "Blerim Rexha", "Valon Raca", "Kadri Sylejmani");
    ObservableList<String> prof1 = FXCollections.observableArrayList("Sisteme te shperndara");
    ObservableList<String> prof2 = FXCollections.observableArrayList("Siguria e te dhenave", "Rrjetet Kompjuterike", "Siguria e Internetit", "Inxhinieria Softuerike");

    ObservableList<String> prof3 = FXCollections.observableArrayList("Arkitektura e kompjuterve", "Programimi ne internet");
    ObservableList<String> prof4 = FXCollections.observableArrayList("Gjuhe programuese", "Algoritmet dhe strukturat e te dhenave","Praktika Profesionale");

    private AddAppointmentRepository appointmentDAO;

    private StudentRepository studentDAO;



    @FXML
    private ObservableList<Konsultimet> allAppointments = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            renderStudent(getStudent());
        } catch (Exception e) {
            ErrorPopupComponent.show(e);
        }
        profesoret.setItems(mainItems);

        profesoret.setOnAction(e ->{
            String selectedItem = profesoret.getSelectionModel().getSelectedItem();

            switch(selectedItem){
                case "Dhurate Hyseni":
                    lendet.setItems(prof1);
                    break;
                case "Blerim Rexha":
                    lendet.setItems(prof2);
                    break;
                case "Valon Raca":
                    lendet.setItems(prof3);
                    break;
                case "Kadri Sylejmani":
                    lendet.setItems(prof4);
                    break;
            }

        });

        datePicker.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date,empty);
                LocalDate today = LocalDate.now();

                if(date.isBefore(today)){
                    setDisable(true);
                }
                if(profesoret.getSelectionModel().getSelectedItem().equals("Dhurate Hyseni")){
                    if(date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.MONDAY || date.getDayOfWeek() == DayOfWeek.WEDNESDAY){
                        setDisable(true);
                    }
                }
                if(profesoret.getSelectionModel().getSelectedItem().equals("Blerim Rexha")){
                    if(date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.MONDAY || date.getDayOfWeek() == DayOfWeek.THURSDAY || date.getDayOfWeek() == DayOfWeek.WEDNESDAY){
                        setDisable(true);
                    }
                }
                if(profesoret.getSelectionModel().getSelectedItem().equals("Valon Raca")){
                    if(date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.MONDAY){
                        setDisable(true);
                    }
                }
                if(profesoret.getSelectionModel().getSelectedItem().equals("Kadri Sylejmani")){
                    if(date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.MONDAY || date.getDayOfWeek() == DayOfWeek.WEDNESDAY){
                        setDisable(true);
                    }
                }

            }
        });

        datePicker.setConverter(new StringConverter<LocalDate>() {
            String format = "MM-dd-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
            {
                datePicker.setPromptText("Pick a date from the picker ");
            }
            @Override
            public String toString(LocalDate localDate) {
                if(localDate != null) {
                    return dateFormatter.format(localDate);
                } else {
                    return " ";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        datePicker.setValue(LocalDate.now());
        date = datePicker.getValue();

        TimeConversion.populateTimes(startTimeComboBox, start, end);
        TimeConversion.populateTimes(endTimeComboBox, start, end);
        startTimeComboBox.getSelectionModel().selectFirst();
        endTimeComboBox.getSelectionModel().select(1);
    }

    public void gatherInformation() {
        name = nameField.getText();
        email = emailField.getText();
        professor = profesoret.getSelectionModel().getSelectedItem();
        lenda = lendet.getSelectionModel().getSelectedItem();
        date = datePicker.getValue();
        startTime =startTimeComboBox.getValue();
        endTime = endTimeComboBox.getValue();
    }

    private boolean validateInformation() throws Exception {
        gatherInformation();
        if(name.isBlank() || email == null || professor == null || lenda == null || date == null || startTime == null || endTime == null){
            Alert missingInformation = new Alert(Alert.AlertType.ERROR);
            missingInformation.setTitle("Missing Information");
            missingInformation.setContentText("Please fill out all fields");
            missingInformation.showAndWait();
            return false;
        }else if(startTime.isAfter(endTime) || startTime.equals(endTime)){
            Alert startEndConflict = new Alert(Alert.AlertType.ERROR);
            startEndConflict.setTitle("Appointment time conflict.");
            startEndConflict.setContentText("Appointment starting time must be before ending time.");
            startEndConflict.showAndWait();
            return false;
        } else if(!overlappingTimes()) {
            return false;
    }else{
            startDateTime = LocalDateTime.of(date, startTime);
            endDateTime = LocalDateTime.of(date, endTime);
            return true;
        }
    }

    private boolean overlappingTimes() throws Exception {
        try {
            startDateTime = LocalDateTime.of(date, startTime);
            endDateTime = LocalDateTime.of(date, endTime);
            appointments = AddAppointmentRepository.getAllKonsultimet();
            for (Konsultimet konsultimi : appointments) {
                if (startDateTime.isAfter(konsultimi.getFillimi().minusMinutes(1)) && startDateTime.isBefore(konsultimi.getFundi())) {
                    Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                    overlapAlert.setTitle("Overlapping appointments");
                    overlapAlert.setContentText("Your appointment overlaps with another appointment");
                    overlapAlert.showAndWait();
                    return false;
                } else if (endDateTime.isAfter(konsultimi.getFillimi().minusMinutes(1)) && endDateTime.isBefore(konsultimi.getFundi())) {
                    Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                    overlapAlert.setTitle("Overlapping appointments");
                    overlapAlert.setContentText("Your appointment overlaps with another appointment");
                    overlapAlert.showAndWait();
                    return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    @FXML
        public void onSubmitButton(ActionEvent e)throws  Exception{
        try {
            if (validateInformation()) {
                AddAppointmentRepository.create(professor, name, email, lenda, startDateTime, endDateTime, LocalDateTime.now());
                Alert addAppointmentInformation = new Alert(Alert.AlertType.INFORMATION);
                addAppointmentInformation.setTitle("Information");
                addAppointmentInformation.setContentText("Appointment added successfully!");
                addAppointmentInformation.showAndWait();
            }
        }catch(Exception ex){
            Alert addAppointmentError = new Alert(Alert.AlertType.ERROR);
            addAppointmentError.setTitle("Error");
            addAppointmentError.setContentText("Failed to add an appointment!");
            addAppointmentError.showAndWait();
            ex.printStackTrace();
        }
    }

    private Student parseRes(ResultSet res) throws SQLException {

        String name = res.getString("name");
        String email = res.getString("email");

        Student student = new Student(name,email) ;
        return student;
    }
    private Student getStudent() throws Exception{
        String sql = "SELECT * FROM student WHERE name = ?";
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,studentName);

        ResultSet res = stmt.executeQuery();

        res.next();
        Student student = parseRes(res);
        return student;
    }

    private void renderStudent(Student student){
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
    }

//    private void checkForAppointment(ActionEvent e){
//        try{
//            appointmentDAO = new AddAppointmentRepository();
//            studentDAO = new StudentRepository();
//            allAppointments = appointmentDAO.getAllKonsultimet();
//
//            boolean appointmentFlag  = false;
//            String apptDate ="";
//            String apptTime = "";
//
//            LocalTime currentTime = LocalTime.now();
//            for(Konsultimet konsultimi : allAppointments){
//                LocalTime appointmentStartTime = konsultimi.getFillimi().toLocalTime();
//                Long timeDifference = ChronoUnit.MINUTES.between(currentTime, appointmentStartTime);
//
//                if (konsultimi.getFillimi().toLocalDate().equals(LocalDate.now())) {
//                    if(timeDifference > -1 && timeDifference <= 15) {
//                        appointmentFlag = true;
//                        System.out.println(appointmentFlag);
//                        apptDate = konsultimi.getStartDateFormatted();
//                        apptTime = konsultimi.getStartTimeFormatted();
//                        break;
//                    }
//                }
//            }
//            if(appointmentFlag){
//                StringBuilder appointmentAlertContext = new StringBuilder();
//                appointmentAlertContext.append("You have an upcoming appointment within 15 minutes.").append("\n");
//                appointmentAlertContext.append("Date: ").append(apptDate).append("\n");
//                appointmentAlertContext.append("Time: ").append(apptTime);
//
//                String title = "Appointment Within 15 Minutes";
//                String body = String.valueOf(appointmentAlertContext);
//                String to = studentEmail;
////                MailController mailController = new MailController(to,title,body);
////                mailController.sendMail();
//                GenericAlert upcomingAppointments = new GenericAlert(title, appointmentAlertContext.toString(), Alert.AlertType.INFORMATION);
//                upcomingAppointments.showAlert(e, "consultation.fxml");
//            }else{
//                String title = "No Upcoming Appointments";
//                String contextText = "You have no upcoming appointments within 15 minutes";
//                String to = studentEmail;
////                MailController mailController = new MailController(to,title,contextText);
////                mailController.sendMail();
//                GenericAlert noUpcomingAppointments = new GenericAlert(title, contextText, Alert.AlertType.INFORMATION);
//                noUpcomingAppointments.showAlert(e, "consultation.fxml");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @FXML
    public void onLogoutButtonClick(ActionEvent e){
        try{
            Alert logOutConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            logOutConfirmation.setTitle("Log Out");
            logOutConfirmation.setContentText("Are you sure you want to logout ?");
            Optional<ButtonType> result = logOutConfirmation.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
            Parent parent = FXMLLoader.load(getClass().getResource("../views/login.fxml"));
            Scene scene = new Scene(parent);

            Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();}

        }catch(Exception ex){

        }
    }
    @FXML
    public void onShikoKonsultimetButtonClick(ActionEvent e) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("../views/consultation.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
