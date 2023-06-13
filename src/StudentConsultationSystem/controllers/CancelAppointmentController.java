package StudentConsultationSystem.controllers;

import StudentConsultationSystem.models.Konsultimet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CancelAppointmentController extends ChildController{

    public static final String CALENDAR_VIEW = "calendar";
    private static final String VIEW_PATH = "../views";
    @FXML
    private VBox contentPane;
    @FXML
    private TextArea textArea;

    private static String subject = "Anulim i konsultimit";

    Konsultimet konsultimet = new Konsultimet();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    public void getCss(Alert alert){
        alert.getDialogPane().getStylesheets().add(getClass().getResource("../resources/styles/style.css").toExternalForm());
        alert.getDialogPane().setStyle("alert");
        alert.setHeight(300);
    }
    @FXML
    public void onSendButtonClick()throws Exception{
        String body = "Pershendetje, <br>Konsultimi ne lenden <b> " + konsultimet.getLenda() + "</b> qe eshte parapare te mbahet me daten <b>"
                + konsultimet.getFillimi().toLocalDate() +  "</b> ne ora <b>" + konsultimet.getFillimi().toLocalTime()
                + "</b> eshte anuluar.<br> <br>Arsyeja e anulimit: " +  textArea.getText();
        MailController mailController = new MailController(konsultimet.getEmail(), subject, body);
        mailController.sendMail();

        Alert sendEmailConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        sendEmailConfirmation.setTitle("Konfirmim");
        sendEmailConfirmation.setContentText("Ju derguat njoftimin pse keni anuluar konsultimin!");
        getCss(sendEmailConfirmation);
        Optional<ButtonType> result = sendEmailConfirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            this.setView(CALENDAR_VIEW);
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
        controller.setCancelAppointmentParentController(this);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(pane);
        contentPane.setVgrow(pane, Priority.ALWAYS);

    }
    private String viewPath(String view){
        return VIEW_PATH + "/" + view + ".fxml";
    }

    public void oldAppointment(Konsultimet selectedItem) {
        this.konsultimet.setEmail(selectedItem.getEmail());
        this.konsultimet.setLenda(selectedItem.getLenda());
        this.konsultimet.setFillimi(selectedItem.getFillimi());
    }
}
