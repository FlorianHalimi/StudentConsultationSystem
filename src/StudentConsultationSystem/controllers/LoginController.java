package StudentConsultationSystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void onLoginButtonClick(ActionEvent e) throws Exception{
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/mainScreen.fxml"));
            Parent parent = loader.load();

            MainController controller = loader.getController();
            controller.setView(MainController.CALENDAR_VIEW);

            Stage primaryStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
