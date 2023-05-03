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
import javafx.scene.chart.PieChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    private BorderPane borderPane;
    ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
    ObservableList<LocalDate> timeList = FXCollections.observableArrayList();
    static String professor = SessionManager.professor.getName();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            borderPane.setCenter(buildPieChart());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private PieChart buildPieChart() throws Exception {
        data = StatisticsRepository.getLenda(professor);
        PieChart pieChart = new PieChart(data);
        pieChart.setTitle("Statistikat e konsultimeve sipas lendeve!"); //Setting the title of the Pie chart
        pieChart.setClockwise(true); //setting the direction to arrange the data
        pieChart.setLabelLineLength(50); //Setting the length of the label line
        pieChart.setLabelsVisible(true); //Setting the labels of the pie chart visible
        pieChart.setLegendVisible(true);
        pieChart.setStartAngle(180);

        data.forEach(item ->{
            item.nameProperty().bind(Bindings.concat(item.getName(), " ", item.pieValueProperty().intValue(), " "));
        });
        return pieChart;
    }

    private PieChart datePieChart() throws Exception {

        timeList = StatisticsRepository.getTimeList(professor);
        PieChart datepieChart = new PieChart();
        datepieChart.setTitle("Statistikat e konsultimeve sipas dates!"); //Setting the title of the Pie chart
        datepieChart.setClockwise(true); //setting the direction to arrange the data
        datepieChart.setLabelLineLength(50); //Setting the length of the label line
        datepieChart.setLabelsVisible(true); //Setting the labels of the pie chart visible
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
