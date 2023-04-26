package StudentConsultationSystem.controllers;

import javafx.fxml.Initializable;

public abstract class ChildController implements Initializable {
    public MainController parentController;
    public LoginController studentLoginParentMainController;
    public  StudentMainController handleChildStudentMainController;
    public CalendarController calendarParentController;

    public CancelAppointmentController cancelAppointmentParentController;

    public ProfileController changePasswordParentController;

    public void setParentController(MainController parentController){
        this.parentController = parentController;
    }

    public void setStudentMainController(LoginController loginMainController){
        this.studentLoginParentMainController = loginMainController;
    }
    public void handleStudentMainController(StudentMainController studentMainController){
        this.handleChildStudentMainController = studentMainController;
    }

    public void setCalendarParentController(CalendarController calendarController){
        this.calendarParentController = calendarController;
    }

    public void setCancelAppointmentParentController(CancelAppointmentController cancelAppointmentController){
        this.cancelAppointmentParentController = cancelAppointmentController;
    }

    public void setChangePasswordParentController(ProfileController changePasswordController){
        this.changePasswordParentController = changePasswordController;
    }

}
