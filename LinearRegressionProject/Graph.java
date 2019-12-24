import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Graph extends Application {
    public void start(Stage stage) {
        Line xAxis = new Line(20, 480, 480, 480);
        xAxis.strokeWidth(3.0);
        Line yAxis = new Line(20, 480, 20, 20);

        Pane root = new Pane();
        // root.setSpacing(0);
        root.getChildren().addAll(xAxis, yAxis);

        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Graph");
        stage.setScene(scene);
        stage.show();
    }
}
