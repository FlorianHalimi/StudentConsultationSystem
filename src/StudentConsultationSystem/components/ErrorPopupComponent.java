package StudentConsultationSystem.components;

import StudentConsultationSystem.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Optional;

public class ErrorPopupComponent {
    public static void show(Exception ex) {
        String message = ex.getMessage();
        if (message == null || message.length() == 0) {
            message = ex.toString();
        }
        show(message);
    }

    public static void show(String message) {
        showError(message, "Error");
    }

    public static void showError(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);

        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(400);
        StackPane pane = new StackPane(label);
        alert.getDialogPane().setContent(pane);

        alert.showAndWait();
    }
    public  static void showInformation(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("SUCCESS!");
        alert.setContentText("Appointment added!");
        alert.showAndWait();
    }

    public static void showLogOutInformation() throws Exception{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("You will log out now!");


        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            SessionManager.professor = null;
        } else return;
    }
    public static boolean showBackButtonInformation() throws Exception{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to leave this page?");


        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            return true;
        } else return false;
    }
}
