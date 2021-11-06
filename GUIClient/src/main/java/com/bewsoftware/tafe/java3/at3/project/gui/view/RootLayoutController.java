/*
 *  File Name:    RootLayoutController.java
 *  Project Name: GUIApp
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
 * Date: 1 Oct 2021
 * ****************************************************************
 */

package com.bewsoftware.tafe.java3.at3.project.gui.view;

import com.bewsoftware.tafe.java3.at3.project.gui.App;
import com.bewsoftware.tafe.java3.at3.project.gui.ViewController;
import com.bewsoftware.tafe.java3.at3.project.gui.Views;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.bewsoftware.tafe.java3.at3.project.gui.Views.BLANK;
import static com.bewsoftware.tafe.java3.at3.project.gui.Views.CHAT;
import static com.bewsoftware.tafe.java3.at3.project.gui.Views.LOGIN;
import static com.bewsoftware.tafe.java3.at3.project.gui.Views.NEW_ACCOUNT;

/**
 * FXML Controller class for the 'RootLayout.fxml' file, which is the main
 * window of the application.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class RootLayoutController implements ViewController
{
    @FXML
    private MenuItem aboutMenuItem;

    private App app;

    @FXML
    private MenuItem chatCloseMenuItem;

    @FXML
    private MenuItem chatOpenMenuItem;

    @FXML
    private MenuItem chatTerminateServerMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    private Views currentView;

    private boolean loggedIn;

    @FXML
    private MenuItem loginMenuItem;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MenuItem newAccountMenuItem;

    @FXML
    private ImageView statusImageView;

    private Image[] statusImages;

    @FXML
    private Label statusLabel;

    /**
     * Instantiate a new copy of RootLayoutController class.
     */
    public RootLayoutController()
    {
        // NoOp
    }

    @Override
    public void setApp(App app)
    {
        this.app = app;
        app.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        switch (evt.getPropertyName())
        {
            case App.PROP_ACTIVEVIEW ->
            {
                // Views being opened:
                currentView = (Views) evt.getNewValue();

                switch ((Views) evt.getNewValue())
                {
                    case BLANK ->
                    {
                    }

                    case CHAT ->
                    {
                        chatOpenMenuItem.setDisable(true);
                        chatCloseMenuItem.setDisable(false);
                        chatTerminateServerMenuItem.setDisable(false);
                    }

                    case LOGIN ->
                    {
                        loginMenuItem.setDisable(true);
                    }

                    case NEW_ACCOUNT ->
                    {
                        newAccountMenuItem.setDisable(true);
                    }

                    default ->
                    {
                    }
                }

                // Views being closed:
                if (evt.getOldValue() != null)
                {
                    switch ((Views) evt.getOldValue())
                    {
                        case BLANK ->
                        {
                        }

                        case CHAT ->
                        {
                            chatOpenMenuItem.setDisable(!loggedIn);
                            chatCloseMenuItem.setDisable(true);
                            chatTerminateServerMenuItem.setDisable(true);
                        }

                        case LOGIN ->
                        {
                            loginMenuItem.setDisable(loggedIn);
                        }

                        case NEW_ACCOUNT ->
                        {
                            newAccountMenuItem.setDisable(loggedIn);
                        }

                        default ->
                        {
                        }
                    }
                }

            }

            case App.PROP_LOGGEDIN ->
            {
                loggedIn = (boolean) evt.getNewValue();

                // Configure affected MenuItems
                if (currentView != CHAT)
                {
                    chatOpenMenuItem.setDisable(!loggedIn);
                }

                loginMenuItem.setDisable(loggedIn);
                logoutMenuItem.setDisable(!loggedIn);
                newAccountMenuItem.setDisable(loggedIn);

                if (loggedIn)
                {
                    // Show green status light
                    statusImageView.setImage(statusImages[StatusImages.GREEN.idx]);
                } else
                {
                    // Show red status light
                    statusImageView.setImage(statusImages[StatusImages.RED.idx]);
                    statusLabel.setText("");
                }

            }

            case App.PROP_STATUSTEXT ->
            {
                statusLabel.setText((String) evt.getNewValue());
            }

            case App.PROP_TERMINATECHATSERVER ->
            {
                chatTerminateServerMenuItem.setDisable(true);

            }

            default ->
            {
            }
        }
    }

    @Override
    public void setFocus()
    {
        // NoOp
    }

    /**
     * Handle the Help/About menu item event.
     *
     * @param event
     */
    @FXML
    private void handleAboutMenuItem(ActionEvent event)
    {
        showAboutDialog();
        event.consume();
    }

    /**
     * Handle the Chat/Close menu item event.
     *
     * @param event
     */
    @FXML
    private void handleChatCloseMenuItem(ActionEvent event)
    {
        app.showView(BLANK);
        event.consume();
    }

    /**
     * Handle the Chat/Open menu item event.
     *
     * @param event
     */
    @FXML
    private void handleChatOpenMenuItem(ActionEvent event)
    {
        app.showView(CHAT);
        event.consume();
    }

    /**
     * Handle the Chat/Shutdown Server menu item event.
     *
     * @param event
     */
    @FXML
    private void handleChatTerminateServerMenuItem(ActionEvent event)
    {
        app.terminateChatServer();
        event.consume();
    }

    /**
     * Handle the File/Close menu item event.
     *
     * @param event
     */
    @FXML
    private void handleCloseMenuItem(ActionEvent event)
    {
        Platform.exit();
        event.consume();
    }

    /**
     * Handle the User/Login menu item event.
     *
     * @param event
     */
    @FXML
    private void handleLoginMenuItem(ActionEvent event)
    {
        app.showView(LOGIN);
        event.consume();
    }

    /**
     * Handle the User/Logout menu item event.
     *
     * @param event
     */
    @FXML
    private void handleLogoutMenuItem(ActionEvent event)
    {
        app.setLoggedIn(false);
        event.consume();
    }

    /**
     * Handle the User/New Account menu item event.
     *
     * @param event
     */
    @FXML
    private void handleNewAccountMenuItem(ActionEvent event)
    {
        app.showView(NEW_ACCOUNT);
        event.consume();
    }

    /**
     * Controller initialization.
     *
     * @throws java.io.IOException         if any
     * @throws java.net.URISyntaxException if any
     */
    @FXML
    private void initialize() throws IOException, URISyntaxException
    {
        chatOpenMenuItem.setDisable(true);
        chatCloseMenuItem.setDisable(true);
        chatTerminateServerMenuItem.setDisable(true);
        logoutMenuItem.setDisable(true);
        statusLabel.setText("");

        statusImages = new Image[]
        {
            new Image(getClass().getResourceAsStream("/images/bullet_red.png"), 18, 18, true, true),
            new Image(getClass().getResourceAsStream("/images/bullet_green.png"), 18, 18, true, true)
        };
    }

    /**
     * Opens the About dialog.
     */
    private void showAboutDialog()
    {
        try
        {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("view/About.fxml"));
            GridPane page = (GridPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("About");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException ex)
        {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Provide convenient indexing into the {@link #statusImages} array.
     */
    private enum StatusImages
    {
        RED(0),
        GREEN(1);

        public final int idx;

        private StatusImages(int idx)
        {
            this.idx = idx;
        }
    }

}
