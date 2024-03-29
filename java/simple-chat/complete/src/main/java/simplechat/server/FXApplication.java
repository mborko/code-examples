package simplechat.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXApplication extends Application {

    public Parent root; // added by hans
    
    private Controller controller;
    private static SimpleChat simpleChat;

    public FXApplication() {}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/server.fxml"));
        root = loader.load(); // changed by hans, declaration moved so root is accessible for mock tests

        this.controller = loader.getController();

        controller.setSimpleChat(simpleChat);
        simpleChat.setController(controller);

        Scene scene = new Scene(root, 700, 300);

        stage.setTitle("Simple Chat - Server");

        stage.setScene(scene);
        stage.show();
    }

    public void setSimpleChat(SimpleChat simpleChat) {
        this.simpleChat = simpleChat;
    }

    @Override
    public void stop(){
        controller.stop();
    }
}
