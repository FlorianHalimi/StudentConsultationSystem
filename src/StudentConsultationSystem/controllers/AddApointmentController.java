package StudentConsultationSystem.controllers;

import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.models.Student;
import StudentConsultationSystem.repositories.AddAppointmentRepository;
import StudentConsultationSystem.repositories.TimeReformation;
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
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    ObservableList<String> prof1 = FXCollections.observableArrayList("Sisteme te shperndara", "Programimi ne internet");
    ObservableList<String> prof2 = FXCollections.observableArrayList("Siguria e te dhenave", "Rrjetet Kompjuterike", "Siguria e Internetit", "Inxhinieria Softuerike");

    ObservableList<String> prof3 = FXCollections.observableArrayList("Arkitektura e kompjuterve");
    ObservableList<String> prof4 = FXCollections.observableArrayList("Gjuhe programuese", "Algoritmet dhe strukturat e te dhenave","Praktika Profesionale");

    private static final List<DayOfWeek> disabledDaysOfWeek = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            renderStudent(getStudent());
            loadDisabledDaysFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
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
                if(date.isEqual(today)){
                    setDisable(true);
                }
                if (date != null && !empty) {
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    if (disabledDaysOfWeek.contains(dayOfWeek)) {
                        setDisable(true);
                    }
                }
            }
        });

        datePicker.setConverter(new StringConverter<LocalDate>() {
            String format = "MM-dd-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
            {
                datePicker.setPromptText("Perzgjedhni daten!");
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

        date = datePicker.getValue();

        TimeReformation.populateTimes(startTimeComboBox, start, end);
        TimeReformation.populateTimes(endTimeComboBox, start, end);
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
            missingInformation.setTitle("Njoftim");
            missingInformation.setContentText("Ju lutem, plotesoni te gjitha fushat.");
            getCss(missingInformation);
            missingInformation.showAndWait();
            return false;
        }else if(endTime.isAfter(startTime.plusMinutes(30))){
            Alert missingInformation = new Alert(Alert.AlertType.ERROR);
            missingInformation.setTitle("Njoftim");
            missingInformation.setContentText("Koha maksimale qe mund te caktoni per konsultim eshte 30 minuta.");
            getCss(missingInformation);
            missingInformation.showAndWait();
            return false;
        }else if(startTime.isAfter(endTime) || startTime.equals(endTime)){
            Alert startEndConflict = new Alert(Alert.AlertType.ERROR);
            startEndConflict.setTitle("Konflikt");
            startEndConflict.setContentText("Koha e fillimit te konsultimit duhet te jete para kohes se mbarimit.");
            getCss(startEndConflict);
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
                    if(konsultimi.getStudent().equals(studentName) || konsultimi.getProfessor().equals(profesoret.getSelectionModel().getSelectedItem())) {
                        Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                        overlapAlert.setTitle("Konflikt i konsultimeve");
                        overlapAlert.setContentText("Ky konsultim nuk mund te caktohet sepse eshte ne konflikt me nje konsultim tjeter tek profesori "
                                + konsultimi.getProfessor() + ", ne lenden " + konsultimi.getLenda());
                        getCss(overlapAlert);
                        overlapAlert.showAndWait();
                        return false;
                    }
                } else if (endDateTime.isAfter(konsultimi.getFillimi().minusMinutes(1)) && endDateTime.isBefore(konsultimi.getFundi())) {
                    if(konsultimi.getStudent().equals(studentName) || konsultimi.getProfessor().equals(profesoret.getSelectionModel().getSelectedItem())) {
                        Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                        overlapAlert.setTitle("Konflikt i konsultimeve");
                        overlapAlert.setContentText("Ky konsultim nuk mund te caktohet sepse eshte ne konflikt me nje konsultim tjeter tek profesori "
                                + konsultimi.getProfessor() + ", ne lenden " + konsultimi.getLenda());
                        getCss(overlapAlert);
                        overlapAlert.showAndWait();
                        return false;
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public void getCss(Alert alert){
        alert.getDialogPane().getStylesheets().add(getClass().getResource("../resources/styles/style.css").toExternalForm());
        alert.getDialogPane().setStyle("alert");
        alert.setHeight(300);
    }
    @FXML
        public void onSubmitButton(ActionEvent e)throws  Exception{
        try {
            if (validateInformation()) {
                AddAppointmentRepository.create(professor, name, email, lenda, startDateTime, endDateTime, LocalDateTime.now());
                Alert addAppointmentInformation = new Alert(Alert.AlertType.INFORMATION);
                addAppointmentInformation.setTitle("Njoftim");
                addAppointmentInformation.setContentText("Konsultimi u shtua me sukses!");
                getCss(addAppointmentInformation);
                addAppointmentInformation.showAndWait();
            }
        }catch(Exception ex){
            Alert addAppointmentError = new Alert(Alert.AlertType.ERROR);
            addAppointmentError.setTitle("Gabim");
            addAppointmentError.setContentText("Deshtoi te shtoj konsultimin!");
            getCss(addAppointmentError);
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


    @FXML
    public void onLogoutButtonClick(ActionEvent e){
        try{
            Alert logOutConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            logOutConfirmation.setTitle("Dil");
            logOutConfirmation.setContentText("A jeni te sigurte qe doni te dilni nga sistemi?");
            DialogPane dialogPane = logOutConfirmation.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("../resources/styles/style.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");
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


    private void loadDisabledDaysFromDatabase() {
        try{
            String query = "SELECT day_of_week FROM disabled_days";
            Connection conn = DbHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                String dayOfWeek = resultSet.getString("day_of_week");
                DayOfWeek disabledDay = DayOfWeek.valueOf(dayOfWeek);
                disabledDaysOfWeek.add(disabledDay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
