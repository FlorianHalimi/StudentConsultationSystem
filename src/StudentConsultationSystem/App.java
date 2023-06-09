package StudentConsultationSystem;


import StudentConsultationSystem.utils.DbHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) throws Exception{
        DbHelper.migrate();
        Application.launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("views/login.fxml"));
        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.setTitle("Sistem per menaxhimin e konsultimeve me studente");
        stage.show();


        stage.sizeToScene();
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());

    }
}
