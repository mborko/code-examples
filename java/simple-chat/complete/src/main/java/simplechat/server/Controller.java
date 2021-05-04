package simplechat.server;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private ListView listView;

    @FXML
    private Text actionTarget = null;

    @FXML
    protected void handleMessageButtonAction(ActionEvent event) {
        event.consume();
        String message = textField.getText();
        if (simpleChat.isConnected() && !message.isEmpty() && simpleChat.getClients().length != 0) {
            simpleChat.sendMessage(message);
            actionTarget.setText("Message sent.");
            scheduledExecutorService.schedule(clearText, 1, TimeUnit.SECONDS);
            // message from the server admin shall also enter the textArea of the server // added by Hans
            updateTextAreaWithText(message); // added by Hans
            textField.clear(); // added by Hans
        }
        else actionTarget.setText("Attention: Message could not be sent!"); // added by hans
    }

    @FXML
    protected void handleRemoveButtonAction(ActionEvent event) {
        event.consume();

        ObservableList<String> clients = listView.getSelectionModel().getSelectedItems();

        if (simpleChat.isConnected() && !clients.isEmpty() && simpleChat.getClients().length != 0) {
            for (String client : clients) {
                actionTarget.setText("User "+client+" removed.");
                simpleChat.shutdownClient(client);
                Platform.runLater(() -> listView.getItems().remove(client));
            }
        }
    }

    public void initialize() {
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

    public void addUser(String user) {
        Platform.runLater(() -> listView.getItems().add(user));
    }

    public void removeUser(String user) {
        Platform.runLater(() -> listView.getItems().remove(user));
    }

    Runnable clearText = () -> {
        actionTarget.setText("");
    };

}
