package StudentConsultationSystem.controllers;

import StudentConsultationSystem.components.ErrorPopupComponent;
import StudentConsultationSystem.models.Professor;
import StudentConsultationSystem.utils.DbHelper;
import StudentConsultationSystem.utils.SessionManager;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileController extends ChildController{

    @FXML
    private VBox contentPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField websiteField;
    @FXML
    private Label lblSuccess;
    @FXML
    private Label lblFail;

    public static final String CHANGE_PASSWORD_VIEW = "changePassword";
    private static final String VIEW_PATH = "../views";
    private String professorName = SessionManager.professor.getName();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{
            renderProfessor(getProfessor());
        }catch (Exception ex){
            ErrorPopupComponent.show(ex);
        }
    }

    private Professor parseRes(ResultSet res) throws SQLException {

        String name = res.getString("name");
        String username = res.getString("username");
        String email = res.getString("email");
        String phone = res.getString("phone");
        String website = res.getString("website");

        Professor professor = new Professor(name, username,email,phone,website) ;
        return professor;
    }
    private Professor getProfessor() throws Exception{
        String sql = "SELECT * from professor WHERE name = ?";
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,professorName);

        ResultSet res = stmt.executeQuery();

        res.next();

        Professor professor = parseRes(res);

        return professor;
    }


    private void renderProfessor(Professor professor){
        usernameField.setText(professor.getUsername());
        nameField.setText(professor.getName());
        emailField.setText(professor.getEmail());
        phoneField.setText(professor.getPhone());
        websiteField.setText(professor.getWebsite());
    }

    @FXML
    public void onUpdateButtonClick(ActionEvent e) throws Exception{
        String username = usernameField.getText();
        String phone = phoneField.getText();
        String website = websiteField.getText();

        if(!matchesTenDigitsNumber(phone)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Phone number must have 9 digits and seperated by hyphens");
            alert.setHeaderText("Wrong phone number format");
            alert.showAndWait();
            renderProfessor(getProfessor());
            return;
        }

        String sql = "UPDATE professor SET phone = ?, website = ? WHERE username = ?";
        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,phone);
        stmt.setString(2,website);
        stmt.setString(3,username);

        try{
            int affectedRows = stmt.executeUpdate();
            lblFail.setVisible(false);
            lblSuccess.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    event ->lblSuccess.setVisible(false)
            );
            visiblePause.play();

            if (affectedRows !=1) throw new Exception("ERR_MULTIPLE_ROWS_AFFECTED");
            renderProfessor(getProfessor());
        }catch(Exception ex){
            lblSuccess.setVisible(false);
            lblFail.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    event ->lblFail.setVisible(false)
            );
            visiblePause.play();

            renderProfessor(getProfessor());
        }

    }
    private static boolean matchesTenDigitsNumber(String s){
        Pattern pattern = Pattern.compile("^\\d{3}-\\d{3}-\\d{3}$");
        Matcher m = pattern.matcher(s);
        return m.matches();
    }

    public void setView(String view)throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(this.viewPath(view)));
        Pane pane = null;
        switch(view){
            case CHANGE_PASSWORD_VIEW:
                pane = loader.load();
                contentPane.setAlignment(Pos.TOP_LEFT);
                break;
            default:
                throw new Exception("ERR_VIEW_NOT_FOUND");
        }

        ChildController controller = loader.getController();
        controller.setChangePasswordParentController(this);

        contentPane.getChildren().clear();
        contentPane.getChildren().add(pane);
        contentPane.setVgrow(pane, Priority.ALWAYS);

    }
    private String viewPath(String view){
        return VIEW_PATH + "/" + view + ".fxml";
    }
    @FXML
    public void hyperLinkAction(ActionEvent e)throws Exception{
        try{
            this.setView(CHANGE_PASSWORD_VIEW);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
