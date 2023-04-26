package StudentConsultationSystem.controllers;

import StudentConsultationSystem.components.ErrorPopupComponent;
import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.models.Student;
import StudentConsultationSystem.repositories.StudentRepository;
import StudentConsultationSystem.utils.DbHelper;
import StudentConsultationSystem.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.security.spec.ECField;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

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
    private TableColumn<Konsultimet, String> todayTimeColumn;
    @FXML
    private TableColumn<Konsultimet, String> otherDaysLendaColumn;

    @FXML
    private TableColumn<Konsultimet, String> otherDaysStudentColumn;
    @FXML
    private TableColumn<Konsultimet, String> otherDaysTimeColumn;

    public static final String EDIT_APPOINTMENT_VIEW = "editAppointment";
    public static final String CANCEL_APPOINTMENT_VIEW = "cancelAppointment";
    private static final String VIEW_PATH = "../views";
    @FXML
    private VBox contentPane;

    private String studentEmail;

    private String professorName = SessionManager.professor.getName();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.todayLendaColumn.setCellValueFactory(new PropertyValueFactory<>("lenda"));
        this.todayStudentiColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        this.todayTimeColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        this.otherDaysLendaColumn.setCellValueFactory(new PropertyValueFactory<>("lenda"));
        this.otherDaysStudentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        this.otherDaysTimeColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        try{
            fillTables();
        }catch(Exception ex){
            ErrorPopupComponent.show(ex);
        }

    }
//    private Konsultimet parseRes(ResultSet res) throws SQLException {
//        Integer konsultimi_id = res.getInt("Konsultimi_Id");
//        String profesori = res.getString("profesori");
//        String studenti = res.getString("studenti");
//        String lenda = res.getString("lenda");
//        String fillimi = res.getString("fillimi");
//        String fundi = res.getString("fundi");
//        String data = res.getString("dita");
//        String email = res.getString("email");
//
//        return new Konsultimet(profesori,studenti,lenda,fillimi,fundi,data,email);
//    }
private static Konsultimet parseRes(ResultSet res) throws SQLException {
    String profesori = res.getString("profesori");
    String studenti = res.getString("studenti");
    String lenda = res.getString("lenda");
    String email = res.getString("email");
    LocalDateTime start = res.getTimestamp("fillimi").toLocalDateTime();
    LocalDateTime end = res.getTimestamp("fundi").toLocalDateTime();
    LocalDateTime creatDate = res.getTimestamp("dita").toLocalDateTime();

    return new Konsultimet(profesori,studenti,lenda,start,end,creatDate,email);
}
    public List<Konsultimet> getKonsultimet(boolean thisDay) throws Exception{
        ArrayList<Konsultimet> konsultimet = new ArrayList<>();
        LocalDate today =LocalDate.now();
        String strToday = today.toString();

        if(thisDay){
            String sql ="SELECT * FROM konsultimet WHERE profesori = '" + professorName + "' and fillimi like '" + strToday + "%';";
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
            String sql = "select * from konsultimet where profesori = '" + professorName + "' and DATE(fillimi) > CURDATE();";
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
    public void onRefreshOption(ActionEvent e) throws Exception {
        fillTables();
    }

    @FXML
    public void onCancelOption(ActionEvent e)throws Exception{
        Konsultimet selected =todayTableView.getSelectionModel().getSelectedItem();
        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("No consultation is selected ! Please select a consultation for cancel-ing.");
            alert.showAndWait();
            return;
        }
        try {
            this.setView(CANCEL_APPOINTMENT_VIEW);
        }catch(Exception ex){
            ErrorPopupComponent.show(ex);
        }
    }

    @FXML
    public void onEditOption(ActionEvent e) throws Exception{
        Konsultimet selected =todayTableView.getSelectionModel().getSelectedItem();
        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("No consultation is selected ! Please select a consultation for edit-ing.");
            alert.showAndWait();
            return;
        }
        try {
            this.setView(EDIT_APPOINTMENT_VIEW);
        }catch(Exception ex){
            ErrorPopupComponent.show(ex);
        }
    }

    public String getEmail(){
        Konsultimet konsultimi = todayTableView.getSelectionModel().getSelectedItem();

        if(konsultimi != null){
            studentEmail = konsultimi.getEmail();
        }
        return studentEmail;
    }


}
