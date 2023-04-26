package StudentConsultationSystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ResourceBundle;

public class CancelAppointmentController extends ChildController{

    @FXML
    private TextArea textArea;

    private static String to = "kdks";
    private static String subject = "Cancel consultation";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    @FXML
    public void onSendButtonClick(ActionEvent e)throws Exception{
        String body = textArea.getText().replaceAll("\n", System.getProperty("line.separator"));;
        MailController mailController = new MailController(to, subject, body);
        mailController.sendMail();
    }


}
