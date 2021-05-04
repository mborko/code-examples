package simplechat.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private SimpleChat simpleChat;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @FXML
    private TextField textField;

    @FXML
    private TextArea textArea;

    @FXML
    private Text actionTarget = null;

    @FXML
    protected void handleMessageButtonAction(ActionEvent event) {
        event.consume();
        String message = textField.getText();
        if (!message.isEmpty()) simpleChat.sendMessage(message);
        if (simpleChat.isConnected()) {
            textField.clear();
            actionTarget.setText("Message sent.");
            scheduledExecutorService.schedule(clearText, 1, TimeUnit.SECONDS);
        }
    }

    public void initialize() {
        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                sendMessage();
            }
        });
        Platform.runLater(() -> textField.requestFocus());
    }

    public void stop() {
        simpleChat.stop();
        scheduledExecutorService.shutdownNow();
    }

    public void setSimpleChat(SimpleChat simpleChat) {
        this.simpleChat = simpleChat;
    }

    public void updateTextAreaWithText(String text) {
        textArea.appendText("\n" + text);
    }

    public void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) simpleChat.sendMessage(message);
        if (simpleChat.isConnected()) {
            textField.clear();
            actionTarget.setText("Message sent.");
            scheduledExecutorService.schedule(clearText, 1, TimeUnit.SECONDS);
        }
    }

    Runnable clearText = () -> {
        actionTarget.setText("");
    };
}
