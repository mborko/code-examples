package info.kofler;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class MainApp extends Application {
      public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello JavaFX!");
        Button btn = new Button();
        btn.setText("Hello JavaFX!");
        btn.setOnAction( (event) -> Platform.exit() );
        Pane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 150));
        primaryStage.show();
    }
}
