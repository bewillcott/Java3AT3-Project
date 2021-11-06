/*
 *  File Name:    ChatController.java
 *  Project Name: GUIClient
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ****************************************************************
 * Name: Bradley Willcott
 * ID:   M198449
 * Date: 8 Oct 2021
 * ****************************************************************
 */

package com.bewsoftware.tafe.java3.at3.project.gui.view;

import com.bewsoftware.tafe.java3.at3.project.gui.App;
import com.bewsoftware.tafe.java3.at3.project.gui.ViewController;
import com.bewsoftware.tafe.java3.at3.project.gui.Views;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import static com.bewsoftware.tafe.java3.at3.project.common.Constants.DISCONNECT_SESSION;
import static com.bewsoftware.tafe.java3.at3.project.common.Constants.SERVER_PORT;
import static com.bewsoftware.tafe.java3.at3.project.common.Constants.TERMINATE_SERVER;
import static com.bewsoftware.tafe.java3.at3.project.gui.Views.CHAT;

/**
 * FXML Controller class for the 'Chat.fxml' file, which is a form for on-line
 * messaging.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class ChatController implements ViewController
{
    private static final String SERVER_NAME = "localhost";

    private App app;

    private BufferedReader incoming; // input stream from server

    @FXML
    private TextArea incomingMessagesTextArea;

    private PrintStream​ outgoing;

    @FXML
    private TextField outgoingMessageTextField;

    @FXML
    private TextArea outgoingMessagesTextArea;

    @FXML
    private Button sendButton;

    private Socket socket;

    public ChatController()
    {
        // NoOp
    }

    @Override
    public void setApp(App app)
    {
        this.app = app;
        app.setStatusText("");
        app.addPropertyChangeListener(this);

        try
        {
            // create socket for connection
            socket = new Socket(SERVER_NAME, SERVER_PORT);

            // get input/output streams
            incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outgoing = new PrintStream​(socket.getOutputStream(), true);

            // Receive incoming reply from Socket Server
            String line = incoming.readLine();
            incomingMessagesTextArea.appendText(line + "\n\n");
        } catch (IOException ex)
        {
            if (ex.getMessage().equals("Connection refused"))
            {
                app.setStatusText("Chat Server: Connection refused!");
                app.terminateChatServer();
            } else
            {
                Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        switch (evt.getPropertyName())
        {
            case App.PROP_ACTIVEVIEW ->
            {
                if ((Views) evt.getOldValue() == CHAT)
                {
                    app.removePropertyChangeListener(this);

                    if (!sendButton.isDisable())
                    {
                        closeConnection(DISCONNECT_SESSION);
                    }

                    app.setStatusText("");
                }

            }

            case App.PROP_LOGGEDIN ->
            {
                sendButton.setDisable(!(boolean) evt.getNewValue());
                closeConnection(DISCONNECT_SESSION);

            }

            case App.PROP_TERMINATECHATSERVER ->
            {
                sendButton.setDisable(true);
                closeConnection(TERMINATE_SERVER);

            }

            default ->
            {
            }
        }
    }

    @Override
    public void setFocus()
    {
        outgoingMessageTextField.requestFocus();
    }

    /**
     * Close connection resources.
     */
    private void closeConnection(String finalMessage)
    {
        if (outgoing != null)
        {
            try
            { // close resources
                outgoing.println(finalMessage);
                incoming.close();
                outgoing.close();
                socket.close();
            } catch (IOException ex)
            {
                Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Handle the Send Button event.
     *
     * @param event
     */
    @FXML
    private void handleSendButton(ActionEvent event)
    {
        String text = outgoingMessageTextField.getText();
        outgoingMessageTextField.clear();

        outgoingMessagesTextArea.appendText(text + "\n");

        // Send text to Socket Server
        outgoing.println(text);

        try
        { // Receive incoming reply from Socket Server
            String line = incoming.readLine();
            incomingMessagesTextArea.appendText(line + "\n");
        } catch (IOException ex)
        {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }

        outgoingMessageTextField.requestFocus();
        event.consume();
    }
}
