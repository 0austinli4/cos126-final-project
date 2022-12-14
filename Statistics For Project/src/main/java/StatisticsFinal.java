import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsFinal extends Application implements Initializable {

    private BorderPane borderPane = new BorderPane();

    public void initialize(URL url, ResourceBundle rb) {

    }

    Button button;

    public static void main(String[] args) {
        launch(args);
    }

    //overriding original start
    public void start(Stage stage) throws Exception {
        stage.setTitle("Title of Window");

        button = new Button();
        button.setText("Clickable!");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        showBarChart();

        Scene scene = new Scene(borderPane, 300, 250);
        stage.setScene(scene);
        stage.show();


    }

    private void showBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Words");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Times said");

        BarChart barChart = new BarChart(xAxis, yAxis);

        XYChart.Series data = new XYChart.Series();
        data.setName("Words Said");

        data.getData().add(new XYChart.Data("Word A", 3000));
        data.getData().add(new XYChart.Data("Word B", 2000));
        data.getData().add(new XYChart.Data("Word C", 1500));

        barChart.getData().add(data);

        borderPane.setCenter(barChart);
    }

    public void handle(ActionEvent event) {

    }
}
