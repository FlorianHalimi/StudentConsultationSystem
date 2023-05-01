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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PieChartController extends ChildController{

    @FXML
    private PieChart pieChart;
    @FXML
    private BorderPane contentPane;
    @FXML
    private BorderPane borderPane;
    static ObservableList<String> data = FXCollections.observableArrayList();
    String professor = SessionManager.professor.getName();
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
        pieChart = new PieChart();

        pieChart.setTitle("Statistikat e konsultimeve sipas lendeve!"); //Setting the title of the Pie chart
        pieChart.setClockwise(true); //setting the direction to arrange the data
//        pieChart.setLabelLineLength(50); //Setting the length of the label line
        pieChart.setLabelsVisible(true); //Setting the labels of the pie chart visible
        pieChart.setLegendVisible(true);
//        pieChart.setStartAngle(180);S

        HashMap<String, Integer> countMap = new HashMap<>();
        for (String category : data) {
            countMap.put(category, countMap.getOrDefault(category, 0) + 1);
        }

        for (String category : countMap.keySet()) {
            int count = countMap.get(category);
            pieChart.getData().add(new PieChart.Data(category + " (" + count + ")", count));
        }

        return pieChart;
    }

}
