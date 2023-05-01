package StudentConsultationSystem.controllers;

import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.repositories.AddAppointmentRepository;
import StudentConsultationSystem.repositories.EditAppointmentRepository;
import StudentConsultationSystem.repositories.TimeConversion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditAppointmentController extends ChildController{
    public static final String CALENDAR_VIEW = "calendar";
    private static final String VIEW_PATH = "../views";

    @FXML
    private VBox contentPane;

    @FXML
    private TextArea textArea;

    @FXML
    private DatePicker datePickerBox;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;

    private LocalTime start = LocalTime.of(9,0);
    private LocalTime end = LocalTime.of(11, 0);

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String text;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private static String subject = "Cancel consultation";

    @FXML
    static
    ObservableList<Konsultimet> appointments = FXCollections.observableArrayList();

    Konsultimet konsultimet = new Konsultimet();

    private DialogPane dialogPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        datePickerBox.setValue(LocalDate.now());
        date = datePickerBox.getValue();

        TimeConversion.populateTimes(startTimeComboBox, start, end);
        TimeConversion.populateTimes(endTimeComboBox, start, end);
        startTimeComboBox.getSelectionModel().selectFirst();
        endTimeComboBox.getSelectionModel().select(1);

    }


    private void gatherInformation(){
        date = datePickerBox.getValue();
        startTime = startTimeComboBox.getValue();
        endTime = endTimeComboBox.getValue();
        text = textArea.getText();
    }

    private boolean overlappingTimes() throws Exception {
        try {
            startDateTime = LocalDateTime.of(date, startTime);
            endDateTime = LocalDateTime.of(date, endTime);
            appointments = EditAppointmentRepository.getAllKonsultimet();
            for (Konsultimet konsultimi : appointments) {
                if (startDateTime.isAfter(konsultimi.getFillimi().minusMinutes(1)) && startDateTime.isBefore(konsultimi.getFundi())) {
                    Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                    overlapAlert.setTitle("Konflikt i konsultimeve");
                    overlapAlert.setContentText("Ky konsultim eshte ne konflikt me konsultimin tek studenti " + konsultimi.getStudent() + ", ne lenden " + konsultimi.getLenda());
                    overlapAlert.showAndWait();
                    return false;
                } else if (endDateTime.isAfter(konsultimi.getFillimi().minusMinutes(1)) && endDateTime.isBefore(konsultimi.getFundi())) {
                    Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                    overlapAlert.setTitle("Konflikt i konsultimeve");
                    overlapAlert.setContentText("Ky konsultim eshte ne konflikt me konsultimin tek studenti " + konsultimi.getStudent() + ", ne lenden " + konsultimi.getLenda());
                    overlapAlert.showAndWait();
                    return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    private boolean validateInformation() throws Exception {
        gatherInformation();
        if(text.isBlank() || date == null || startTime == null || endTime == null){
            Alert missingInformation = new Alert(Alert.AlertType.ERROR);
            missingInformation.setTitle("Mungese te informatave");
            missingInformation.setContentText("Ju lutem, plotesoni te gjitha fushat");
            missingInformation.showAndWait();
            return false;
        }else if(startTime.isAfter(endTime) || startTime.equals(endTime)){
            Alert startEndConflict = new Alert(Alert.AlertType.ERROR);
            startEndConflict.setTitle("Konflikt ne kohen e konsultimeve");
            startEndConflict.setContentText("Koha e fillimit te konsultimit duhet te jete para kohes se mbarimit.");
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

    @FXML
    public void onDergoButtonClick(ActionEvent e)throws Exception{
        try {
            if (validateInformation()) {
                EditAppointmentRepository.UpdateAppointment(startDateTime, endDateTime, LocalDateTime.now(), konsultimet.getKonsultimi_id());
                Alert addAppointmentInformation = new Alert(Alert.AlertType.INFORMATION);
                dialogPane = addAppointmentInformation.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("style.css").toString());
                dialogPane.getStyleClass().add("dialog");
                addAppointmentInformation.setTitle("Njoftim");
                addAppointmentInformation.setContentText("Konsultimi u ndryshua me sukses!");
                addAppointmentInformation.showAndWait();

                String body = "Arsyeja e ndryshimit te konsultimit: " +textArea.getText() + "\n" + "Data: " + konsultimet.getData() + "\n" + "Koha: " + konsultimet.getFillimi();
                MailController mailController = new MailController(konsultimet.getEmail(), subject, body);
                mailController.sendMail();

                Alert sendEmailConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                sendEmailConfirmation.setTitle("Konfirmim");
                sendEmailConfirmation.setContentText("Ju derguat njoftimin pse keni anuluar konsultimin!");
                Optional<ButtonType> result = sendEmailConfirmation.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK){
                    this.setView(CALENDAR_VIEW);
                }
            }
        }catch(Exception ex){
            Alert addAppointmentError = new Alert(Alert.AlertType.ERROR);
            addAppointmentError.setTitle("Gabim");
            addAppointmentError.setContentText("Deshtoi ne ndryshimin e konsultimit!");
            addAppointmentError.showAndWait();
            ex.printStackTrace();
        }
    }
    public void setView(String view)throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(this.viewPath(view)));
        Pane pane = null;
        switch(view){
            case CALENDAR_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            default:
                throw new Exception("ERR_VIEW_NOT_FOUND");
        }

        ChildController controller = loader.getController();
        controller.setEditAppointmentController(this);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(pane);
        contentPane.setVgrow(pane, Priority.ALWAYS);

    }
    private String viewPath(String view){
        return VIEW_PATH + "/" + view + ".fxml";
    }

    public void oldAppointment(Konsultimet selectedItem) {
        this.konsultimet.setKonsultimi_id(selectedItem.getKonsultimi_id());
        this.konsultimet.setEmail(selectedItem.getEmail());
        this.konsultimet.setData(selectedItem.getData());
        this.konsultimet.setFillimi(selectedItem.getFillimi());
    }
}
