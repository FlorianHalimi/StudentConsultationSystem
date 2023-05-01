package StudentConsultationSystem.controllers;

import StudentConsultationSystem.components.ErrorPopupComponent;
import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.repositories.AddAppointmentRepository;
import StudentConsultationSystem.utils.DbHelper;
import StudentConsultationSystem.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConsultationController extends ChildController{

    @FXML
    private TableView<Konsultimet> konsultimetTableView;
    @FXML
    private TableColumn<Konsultimet, String> LendaColumn;

    @FXML
    private TableColumn<Konsultimet, String> ProfessorColumn;

    @FXML
    private TableColumn<Konsultimet, LocalDateTime> TimeColumn;

    private String studentName = SessionManager.student.getName();
    @FXML
    private ObservableList<Konsultimet> allAppointments = FXCollections.observableArrayList();

    private AddAppointmentRepository addAppointmentRepository = new AddAppointmentRepository();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.LendaColumn.setCellValueFactory(new PropertyValueFactory<>("lenda"));
        this.ProfessorColumn.setCellValueFactory(new PropertyValueFactory<>("professor"));
        this.TimeColumn.setCellValueFactory(new PropertyValueFactory<>("fillimi"));

        try{
            fillTables();
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            allAppointments = addAppointmentRepository.getAllKonsultimet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static Konsultimet parseRes(ResultSet res) throws SQLException {
        Integer konsultimiId = res.getInt("Konsultimi_id");
        String profesori = res.getString("profesori");
        String studenti = res.getString("studenti");
        String lenda = res.getString("lenda");
        String email = res.getString("email");
        LocalDateTime start = res.getTimestamp("fillimi").toLocalDateTime();
        LocalDateTime end = res.getTimestamp("fundi").toLocalDateTime();
        LocalDateTime creatDate = res.getTimestamp("dita").toLocalDateTime();

        return new Konsultimet(profesori,studenti,lenda,konsultimiId,start,end,creatDate,email);
    }
    public List<Konsultimet> getKonsultimet() throws Exception{
        ArrayList<Konsultimet> konsultimet = new ArrayList<>();

            String sql = "select * from konsultimet where Studenti = '" + studentName + "' and DATE(fillimi) >= CURDATE();";
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

        return konsultimet;
    }

    public void fillTables() throws Exception{
        List<Konsultimet> konsultimetToday = getKonsultimet();
        konsultimetTableView.getItems().clear();
        konsultimetTableView.setItems(FXCollections.observableArrayList(konsultimetToday));
    }

    @FXML
    public void onDeleteOption(ActionEvent e) throws Exception {
            try{
                Konsultimet selectedItem =konsultimetTableView.getSelectionModel().getSelectedItem();
                int selectedIndex = konsultimetTableView.getSelectionModel().getSelectedIndex();
                int konsultimiID =selectedItem.getKonsultimi_id();
                Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                deleteConfirmation.setTitle("Konfirmim");
                deleteConfirmation.setContentText("A jeni te sigurte qe doni te fshini kete konsultim?");
                Optional<ButtonType> result = deleteConfirmation.showAndWait();

                if(result.isPresent() && result.get() == ButtonType.OK){
                    allAppointments.remove(selectedItem);
                    addAppointmentRepository.DeleteAppointment(konsultimiID);
                    Alert appointmentCancellation = new Alert(Alert.AlertType.INFORMATION);
                    appointmentCancellation.setTitle("Anulim");
                    String appointmentCancellationContext = "Konsultimi u anulua.";
                    appointmentCancellation.setContentText(appointmentCancellationContext);
                    konsultimetTableView.getItems().remove(selectedIndex);
                    allAppointments.remove(selectedItem);
                    appointmentCancellation.showAndWait();

                }
            }catch(Exception ex){
                Alert selectAppointment = new Alert(Alert.AlertType.ERROR);
                selectAppointment.setTitle("Zgjedhni nje konsultim");
                selectAppointment.setContentText("Ju lutem, zgjidhni nje konsultim qe doni te fshini nga tabela.");
                selectAppointment.showAndWait();
                ex.printStackTrace();
            }
    }
    @FXML
    public void onBackButtonClick(ActionEvent e) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("../views/addAppointment.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
