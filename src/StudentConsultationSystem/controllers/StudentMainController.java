package StudentConsultationSystem.controllers;

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

public class StudentMainController implements Initializable {
    public static final String ADD_APPOINTMENT_VIEW = "addAppointment";
    public static final String CONSULTATION_VIEW = "consultation";
    private static final String VIEW_PATH = "../views";
    @FXML
    private VBox contentPane;
    @FXML
    private Label statusLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private String viewPath(String view){
        return VIEW_PATH + "/" + view + ".fxml";
    }
    public void setView(String view)throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(this.viewPath(view)));
        Pane pane = null;
        switch(view){
            case ADD_APPOINTMENT_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            case CONSULTATION_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            default:
                throw new Exception("ERR_VIEW_NOT_FOUND");
        }

        ChildController controller = loader.getController();
        controller.handleStudentMainController(this);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(pane);
        contentPane.setVgrow(pane, Priority.ALWAYS);

    }

    @FXML
    public void onConsultationButtonClick(ActionEvent e){
        try{
            this.setView(ADD_APPOINTMENT_VIEW);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    public void onLogoutButtonClick(ActionEvent e){
        try{
            Parent parent = FXMLLoader.load(getClass().getResource(viewPath("login")));
            Scene scene = new Scene(parent);

            Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception ex){

        }
    }
}
