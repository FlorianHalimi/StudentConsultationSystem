package StudentConsultationSystem.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public static final String CALENDAR_VIEW = "calendar";
    public static final String PROFILE_VIEW = "profile";
    public static final String EDIT_APPOINTMENT_VIEW = "editAppointment";
    public static final String CANCEL_APPOINTMENT_VIEW = "cancelAppointment";

    private static final String VIEW_PATH = "../views";
    @FXML
    private VBox contentPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                contentPane.setAlignment(Pos.TOP_CENTER);
                break;
            default:
                throw new Exception("ERR_VIEW_NOT_FOUND");
        }

        contentPane.getChildren().clear();
        contentPane.getChildren().add(pane);
        contentPane.setVgrow(pane, Priority.ALWAYS);

    }
    private String viewPath(String view){
        return VIEW_PATH + "/" + view + ".fxml";
    }
}
