package StudentConsultationSystem.controllers;

import StudentConsultationSystem.models.Konsultimet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LinkController extends ChildController{
    @FXML
    private Button Dergo;
    @FXML
    private TextField linkField;
    @FXML
    private VBox contentPane;

    public static final String CALENDAR_VIEW = "calendar";
    private static final String VIEW_PATH = "../views";

    private static String subject = "Linku per konsultim";

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
    public void onDergoButtonClick(ActionEvent e)throws  Exception{
        String body = "Pershdendetje, <br>Te bashkangjitur e keni linkun: <b>" +linkField.getText() +
                "</b> per konsultimin ne lenden <b>"+ konsultimet.getLenda() + "</b> qe eshte parapare te mbahet me daten <b>"
                + konsultimet.getFillimi().toLocalDate() + "</b> ne ora <b>" + konsultimet.getFillimi().toLocalTime() + "</b>";
        MailController mailController = new MailController(konsultimet.getEmail(), subject, body);
        mailController.sendMail();

        Alert sendEmailConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        sendEmailConfirmation.setTitle("Konfirmim");
        sendEmailConfirmation.setContentText("Ju derguat linkun per mbajtjen e konsultimit!");
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
        controller.setLinkController(this);

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
