package StudentConsultationSystem.controllers;

import StudentConsultationSystem.components.ErrorPopupComponent;
import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.repositories.AddAppointmentRepository;
import StudentConsultationSystem.repositories.EditAppointmentRepository;
import StudentConsultationSystem.utils.DbHelper;
import StudentConsultationSystem.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CalendarController extends ChildController{

    @FXML
    private TableView<Konsultimet> todayTableView;

    @FXML
    private TableView<Konsultimet> otherDaysTableView;

    @FXML
    private TableColumn<Konsultimet, String> todayLendaColumn;

    @FXML
    private TableColumn<Konsultimet, String> todayStudentiColumn;
    @FXML
    private TableColumn<Konsultimet, LocalDateTime> todayTimeColumn;
    @FXML
    private TableColumn<Konsultimet, String> otherDaysLendaColumn;

    @FXML
    private TableColumn<Konsultimet, String> otherDaysStudentColumn;
    @FXML
    private TableColumn<Konsultimet, LocalDateTime> otherDaysTimeColumn;

    @FXML
    private Button cancelButton;

    @FXML
    private Button editButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button sendLinkButton;


    public static final String EDIT_APPOINTMENT_VIEW = "editAppointment";
    public static final String CANCEL_APPOINTMENT_VIEW = "cancelAppointment";
    public static final String CALENDAR_VIEW = "calendar";
    private static final String VIEW_PATH = "../views";
    @FXML
    private VBox contentPane;

    private String professorName = SessionManager.professor.getName();
    @FXML
    private ObservableList<Konsultimet> allAppointments = FXCollections.observableArrayList();
    private AddAppointmentRepository addAppointmentRepository = new AddAppointmentRepository();
    private EditAppointmentRepository editAppointmentRepository = new EditAppointmentRepository();

    private DialogPane dialogPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.todayLendaColumn.setCellValueFactory(new PropertyValueFactory<>("lenda"));
        this.todayStudentiColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        this.todayTimeColumn.setCellValueFactory(new PropertyValueFactory<>("fillimi"));
        this.otherDaysLendaColumn.setCellValueFactory(new PropertyValueFactory<>("lenda"));
        this.otherDaysStudentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        this.otherDaysTimeColumn.setCellValueFactory(new PropertyValueFactory<>("fillimi"));

        try{
            fillTables();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        todayTableView.setOnMouseClicked(e -> {
            sendLinkButton.setDisable(false);
            cancelButton.setDisable(true);
            editButton.setDisable(true);
        });

        otherDaysTableView.setOnMouseClicked(e -> {
            cancelButton.setDisable(false);
            editButton.setDisable(false);
            sendLinkButton.setDisable(true);
        });

        try {
            allAppointments = addAppointmentRepository.getAllKonsultimet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
private Konsultimet parseRes(ResultSet res) throws SQLException {
    Integer konsultimiID = res.getInt("Konsultimi_id");
    String profesori = res.getString("profesori");
    String studenti = res.getString("studenti");
    String lenda = res.getString("lenda");
    String email = res.getString("email");
    LocalDateTime start = res.getTimestamp("fillimi").toLocalDateTime();
    LocalDateTime end = res.getTimestamp("fundi").toLocalDateTime();
    LocalDateTime data = res.getTimestamp("dita").toLocalDateTime();

    return new Konsultimet(profesori,studenti,lenda,konsultimiID,start,end,data,email);
}
    public List<Konsultimet> getKonsultimet(boolean thisDay) throws Exception{
        ArrayList<Konsultimet> konsultimet = new ArrayList<>();
        LocalDate today =LocalDate.now();

        if(thisDay){
            String sql ="SELECT * FROM konsultimet WHERE Profesori = '" + professorName + "' and fillimi like '" + today + "%';";
            try{
                Connection conn = DbHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet res = stmt.executeQuery();
                while(res.next()){
                    konsultimet.add(parseRes(res));
                }
            }catch(Exception ex){
                ErrorPopupComponent.show(ex);
            }
        }else{
            String sql = "select * from konsultimet where Profesori = '" + professorName + "' and DATE(fillimi) > CURDATE();";
            try {
                Connection conn = DbHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet res = stmt.executeQuery();

                while (res.next()) {
                    konsultimet.add(parseRes(res));
                }
            }catch(Exception ex){
                ErrorPopupComponent.show(ex);
            }
        }
        return konsultimet;
    }

    public void fillTables() throws Exception{
        List<Konsultimet> konsultimetToday = getKonsultimet(true);
        todayTableView.getItems().clear();
        todayTableView.setItems(FXCollections.observableArrayList(konsultimetToday));

        List<Konsultimet> konsultimetOtherDays = getKonsultimet(false);
        otherDaysTableView.getItems().clear();
        otherDaysTableView.setItems(FXCollections.observableArrayList(konsultimetOtherDays));

    }

    public void setView(String view)throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(this.viewPath(view)));
        Pane pane = null;
        switch(view){
            case EDIT_APPOINTMENT_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            case CANCEL_APPOINTMENT_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            case CALENDAR_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            default:
                throw new Exception("ERR_VIEW_NOT_FOUND");
        }

        ChildController controller = loader.getController();
        controller.setCalendarParentController(this);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(pane);
        contentPane.setVgrow(pane, Priority.ALWAYS);

    }
    private String viewPath(String view){
        return VIEW_PATH + "/" + view + ".fxml";
    }



    @FXML
    public void onRefreshButtonClick(ActionEvent e) throws Exception {
        fillTables();
    }

    @FXML
    public void onCancelButtonClick(ActionEvent e) throws Exception {
        Konsultimet selectedItem = otherDaysTableView.getSelectionModel().getSelectedItem();
        int selectedIndex = otherDaysTableView.getSelectionModel().getSelectedIndex();
        int konsultimiID = selectedItem.getKonsultimi_id();

        Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirmation.setTitle("Konfirmimi i anulimit");
        deleteConfirmation.setContentText("A jeni te sigurt qe doni te anuloni kete konsultim?");
        Optional<ButtonType> result = deleteConfirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            allAppointments.remove(selectedItem);
            addAppointmentRepository.DeleteAppointment(konsultimiID);
            Alert appointmentCancellation = new Alert(Alert.AlertType.INFORMATION);
            appointmentCancellation.setTitle("Konsultimi u anulua");
            String appointmentCancellationContext = "Konsultimi u anulua.";
            appointmentCancellation.setContentText(appointmentCancellationContext);
            otherDaysTableView.getItems().remove(selectedIndex);
            allAppointments.remove(selectedItem);
            appointmentCancellation.showAndWait();

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../views/cancelAppointment.fxml"));
                Pane pane = loader.load();
                CancelAppointmentController controller =loader.getController();
                controller.oldAppointment(selectedItem);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(pane);
                contentPane.setVgrow(pane, Priority.ALWAYS);
                contentPane.setAlignment(Pos.TOP_LEFT);
                Alert notify = new Alert(Alert.AlertType.INFORMATION);
                notify.setTitle("Informacion");
                notify.setContentText("Ju duhet te tregoni arsyen e anulimit te konsulimit.");
                notify.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void onEditButtonClick(ActionEvent e){
        Konsultimet appointmentToModify = otherDaysTableView.getSelectionModel().getSelectedItem();
        Alert editConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        editConfirmation.setTitle("Konfirmimi i ndryshimit");
        editConfirmation.setContentText("A jeni te sigurt qe doni te beni ndonje ndryshim rreth ketij konsultimi?");
        DialogPane dialogPane = editConfirmation.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("../resources/styles/style.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        Optional<ButtonType> result = editConfirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../views/editAppointment.fxml"));
                Pane pane = loader.load();
                EditAppointmentController controller =loader.getController();
                controller.oldAppointment(appointmentToModify);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(pane);
                contentPane.setVgrow(pane, Priority.ALWAYS);
                contentPane.setAlignment(Pos.TOP_LEFT);
                Alert notify = new Alert(Alert.AlertType.INFORMATION);
                notify.setTitle("Informacion");
                notify.setContentText("Ju duhet te tregoni arsyen e anulimit te konsulimit.");
                notify.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void onSendLinkButtonClick(ActionEvent e) throws Exception {
        Konsultimet appointmentToSendLink = todayTableView.getSelectionModel().getSelectedItem();

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../views/link.fxml"));
                Pane pane = loader.load();
                LinkController controller =loader.getController();
                controller.oldAppointment(appointmentToSendLink);
                contentPane.getChildren().clear();
                contentPane.getChildren().add(pane);
                contentPane.setVgrow(pane, Priority.ALWAYS);
                contentPane.setAlignment(Pos.TOP_LEFT);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


}
