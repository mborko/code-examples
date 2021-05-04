package submission.simplechat.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasChildren;

import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import simplechat.server.FXApplication;
import simplechat.server.SimpleChat;

import java.util.ArrayList;

/**
 * The user interface
 *
 * @author Kai Hoeher {@literal <khoeher@tgm.ac.at>}
 * @author Michael Borko {@literal <mborko@tgm.ac.at>}
 * @author Hans Brabenetz {@literal <hbrabenetz@tgm.ac.at>}
 * @version 1.0
 */
public class FXApplicationMockTest extends ApplicationTest {

    private Parent root;
    private ObservableList<String> text = FXCollections.observableArrayList();
    private MockClient mockClient;
    private static SimpleChat simpleChat;
    private static int iteration = 0;
    private static ArrayList<MockClient> arrayMockClient;

    @Override
    public void start(Stage stage) throws Exception {

        for (; iteration == 0; ++iteration) {
            simpleChat = new SimpleChat("localhost", 8888);
            simpleChat.listen();
            arrayMockClient = new ArrayList<MockClient>();
        }

        FXApplication fxApplication = new FXApplication();
        fxApplication.setSimpleChat(simpleChat);

        fxApplication.start(stage);

        root = fxApplication.root;

        sleep(1000); // client may only connect successfully when server stuff is already done
        mockClient = new MockClient();
        arrayMockClient.add(mockClient);
    }


    @Before
    public void setUp() throws Exception {
    }


    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }


    @Test
    public void testIsClientConnected() {
        // is the latest client connected?
        assert (mockClient.clientSocket.isConnected());
    }

    @Test
    public void testClientSentMessage() {
        String msg = "This is a test!";
        mockClient.send(msg);
        TextArea textArea = (TextArea) GuiTest.find("#textArea");
        sleep(1000);
        String text = textArea.getText();
        assert (text.endsWith(msg));
    }

    @Test
    public void testHasButtonRemove() {
        verifyThat("#grid", hasChildren(1, "#btnRemove"));
    }


    @Test
    public void testWelcomeMessage() {
        TextArea textArea = (TextArea) GuiTest.find("#textArea");
        assertThat(textArea.getText(), is("Welcome to Simple Chat!"));
    }


    @Test
    public void testClientGotMessage() {
        TextArea textArea = (TextArea) GuiTest.find("#textArea");
        clickOn("#textField");
        write("This is a test!");
        clickOn("#btnSend"); // would crash since socket is not up and running
        sleep(1000);
        assertThat(mockClient.received.get(mockClient.received.size() - 1), is("This is a test!"));
    }



    @Test
    public void testServerShowsOwnMessageInHisGUI() {
        String msg = "This is a test!";
        TextArea textArea = (TextArea) GuiTest.find("#textArea");
        clickOn("#textField");
        write(msg);
        clickOn("#btnSend"); // would crash since socket is not up and running
        sleep(1000);
        String text = textArea.getText();
        assert (text.endsWith(msg));
    }

    @Test
    public void testClientSentMessageByName() {
        String msg = "[Client's Name] This is a test!";
        mockClient.send(msg);
        TextArea textArea = (TextArea) GuiTest.find("#textArea");
        sleep(1000);
        String text = textArea.getText();
        assert (text.endsWith(msg));
    }

    @Test
    public void testClientsSendMessages() {
        String msg = "This is a test!";
        for (MockClient m : arrayMockClient)
            m.send(msg);
        TextArea textArea = (TextArea) GuiTest.find("#textArea");
        sleep(1000);
        String text = textArea.getText();
        assert (text.endsWith(msg));
    }

}
