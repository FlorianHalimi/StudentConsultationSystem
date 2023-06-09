package StudentConsultationSystem.controllers;

import StudentConsultationSystem.models.Konsultimet;
import StudentConsultationSystem.repositories.StatisticsRepository;
import StudentConsultationSystem.utils.DbHelper;
import StudentConsultationSystem.utils.SessionManager;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PieChartController extends ChildController{


    @FXML
    private Button button;

    @FXML
    private Button backButton;
    @FXML
    private BorderPane borderPane;

    ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
    ObservableList<LocalDate> timeList = FXCollections.observableArrayList();

    static String professor = SessionManager.professor.getName();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            borderPane.setCenter(buildPieChart());
            backButton.setVisible(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private PieChart buildPieChart() throws Exception {
        data = StatisticsRepository.getLenda(professor);
        PieChart pieChart = new PieChart(data);
        pieChart.setTitle("Statistikat e konsultimeve sipas lendeve!");
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setStartAngle(0);

        data.forEach(item ->{
            item.nameProperty().bind(Bindings.concat(item.getName(), " ", item.pieValueProperty().intValue(), " "));
        });
        if(data.isEmpty()){
            Alert noConsultationRegistered = new Alert(Alert.AlertType.INFORMATION);
            noConsultationRegistered.setTitle("Njoftim");
            noConsultationRegistered.setContentText("Nuk keni ndonje konsultim te regjistuar!");
            noConsultationRegistered.getDialogPane().getStylesheets().add(
                    getClass().getResource("../resources/styles/style.css").toExternalForm());
            noConsultationRegistered.getDialogPane().setStyle("alert");
            noConsultationRegistered.showAndWait();
        }
        return pieChart;
    }
    private PieChart datePieChart() throws Exception {
        timeList = StatisticsRepository.getTimeList(professor);
        PieChart datepieChart = new PieChart();
        datepieChart.setTitle("Statistikat e konsultimeve sipas dates!");
        datepieChart.setClockwise(true);
        datepieChart.setLabelLineLength(50);
        datepieChart.setLabelsVisible(true);
        datepieChart.setLegendVisible(true);
        datepieChart.setStartAngle(180);
        datepieChart.getData().clear();
        datepieChart.getData().remove(timeList);
        HashMap<LocalDate, Integer> countMap = new HashMap<>();

        countMap.clear();
        for (LocalDate category : timeList) {
            countMap.put(LocalDate.parse(String.valueOf(category)), countMap.getOrDefault(category, 0) + 1);
        }

        for (LocalDate category : countMap.keySet()) {
            int count = countMap.get(category);
            datepieChart.getData().add(new PieChart.Data(category + " (" + count + ")", count));
        }
        countMap.clear();
        return datepieChart;
    }

    @FXML
    public void onStatisticsBasedOnDate(){
        try {
            borderPane.setCenter(datePieChart());
            button.setVisible(false);
            backButton.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onBackIconClick(){
        try {
            borderPane.setCenter(buildPieChart());
            backButton.setVisible(false);
            button.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
