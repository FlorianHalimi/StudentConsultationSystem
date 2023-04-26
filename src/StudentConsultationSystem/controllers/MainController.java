package StudentConsultationSystem.controllers;

import StudentConsultationSystem.components.AboutComponent;
import StudentConsultationSystem.components.ErrorPopupComponent;
import StudentConsultationSystem.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public static final String CALENDAR_VIEW = "calendar";
    public static final String PROFILE_VIEW = "profile";
    public static final String EDIT_APPOINTMENT_VIEW = "editAppointment";
    public static final String CANCEL_APPOINTMENT_VIEW = "cancelAppointment";
    public static final String ADD_APPOINTMENT_VIEW = "addAppointment";

    private static final String VIEW_PATH = "../views";
    @FXML
    private VBox contentPane;
    @FXML
    private Label statusLabel;
    CalendarController calendarController = new CalendarController();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String statusText = "%s";
        String professor = SessionManager.professor.getName();
        statusLabel.setText(String.format(statusText,professor));
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
            case PROFILE_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            case EDIT_APPOINTMENT_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            case CANCEL_APPOINTMENT_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            case ADD_APPOINTMENT_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            default:
                throw new Exception("ERR_VIEW_NOT_FOUND");
        }

        ChildController controller = loader.getController();
        controller.setParentController(this);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(pane);
        contentPane.setVgrow(pane, Priority.ALWAYS);

    }
    private String viewPath(String view){
        return VIEW_PATH + "/" + view + ".fxml";
    }

    @FXML
    public void onProfileButtonClick(ActionEvent e){
        try{
            this.setView(PROFILE_VIEW);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    public void onCalendarButtonClick(ActionEvent e){
        try{
            this.setView(CALENDAR_VIEW);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    public void onLogoutButtonClick(ActionEvent e){
        try{
            ErrorPopupComponent.showLogOutInformation();
            Parent parent = FXMLLoader.load(getClass().getResource(viewPath("login")));
            Scene scene = new Scene(parent);

            Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch(Exception ex){

        }
    }

    @FXML
    public void onLogOutMenuClick (ActionEvent ev){
        try{

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void onExitMenuClick (ActionEvent ev){
        try{

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void onRescheduleMenuClick(ActionEvent e){
        try{
            this.setView(EDIT_APPOINTMENT_VIEW);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    public void onCancelMenuClick(ActionEvent e){
        try{
            this.setView(CANCEL_APPOINTMENT_VIEW);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void onAboutMenuClick (ActionEvent ev){
        try{
             new AboutComponent().showDialog();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onRefreshButtonClick(ActionEvent e) throws Exception {
        calendarController.fillTables();
    }
    @FXML
    public void onBackButtonClick(ActionEvent e) throws Exception {
        this.setView(CALENDAR_VIEW);
    }
}
