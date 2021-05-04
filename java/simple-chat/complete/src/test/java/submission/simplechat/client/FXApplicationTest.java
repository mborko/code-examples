package submission.simplechat.client;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasChildren;
import static org.testfx.matcher.base.NodeMatchers.hasText;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.testfx.api.FxToolkit;

import org.testfx.framework.junit.ApplicationTest;

import org.loadui.testfx.GuiTest;
import simplechat.client.FXApplication;

/**
 * Testing the Client user interface
 *
 * @author Kai Hoeher {@literal <khoeher@tgm.ac.at>}
 * @author Michael Borko {@literal <mborko@tgm.ac.at>}
 * @author Hans Brabenetz {@literal <hbrabenetz@tgm.ac.at>}
 * @version 1.0
 */

public class FXApplicationTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(FXApplication.class.getResource("/client.fxml"));

        stage.setScene(new Scene(mainNode, 300, 275));
        stage.show();
        stage.toFront();
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
    public void testHasTextArea() {
        verifyThat("#grid", hasChildren(1, "#textArea"));
    }

    @Test
    public void testHasTextField() {
        verifyThat("#grid", hasChildren(1, "#textField"));
    }

    @Test
    public void testHasHBox() {
        verifyThat("#grid", hasChildren(1, "#hBox"));
    }

    @Test
    public void testHasActionTarget() {
        verifyThat("#grid", hasChildren(1, "#actionTarget"));
    }

    @Test
    public void testHasButton() {
        verifyThat("#hBox", hasChildren(1, "#btn"));
    }

    @Test
    public void testWelcomeMessage() {
        verifyThat("#textArea", hasText("Welcome to Simple Chat!"));
    }

    @Test
    public void testSendButtonVisible() {
        verifyThat("#btn", Node::isVisible);
    }

    @Test
    public void testSendButtonText() {
        verifyThat("#btn", hasText("Send"));
    }

    @Test
    public void testTextFieldEditable() {
        TextField txtFld = (TextField) GuiTest.find("#textField");
        assert (txtFld.isEditable());
    }

    @Test
    public void testTextAreaVisible() {
        verifyThat("#textArea", Node::isVisible);
    }

    @Test
    public void testTextAreaNotEditable() {
        TextArea txtArea = (TextArea) GuiTest.find("#textArea");
        assert (!txtArea.isEditable());
    }

    @Test
    public void testInput() {
        TextField txtFld = (TextField) GuiTest.find("#textField");
        clickOn("#textField");
        write("This is a test!");
        // is not jet a strong test since it only checks what it has written before into the same field
        assertThat(txtFld.getText(), is("This is a test!"));
        // clickOn("#btn"); // would crash since socket is not up and running
    }


}
