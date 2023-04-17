package StudentConsultationSystem.controllers;

import javafx.fxml.Initializable;

public abstract class ChildController implements Initializable {
    public MainController parentController;

    public void setParentController(MainController parentController){
        this.parentController = parentController;
    }
}
