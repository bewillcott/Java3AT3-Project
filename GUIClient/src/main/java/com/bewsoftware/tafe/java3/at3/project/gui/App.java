/*
 *  File Name:    App.java
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
 * Date: 7 Oct 2021
 * ****************************************************************
 */

package com.bewsoftware.tafe.java3.at3.project.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static com.bewsoftware.tafe.java3.at3.project.common.Constants.PROJECT_TITLE;
import static javafx.application.Application.launch;

/**
 * JavaFX App
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class App extends Application
{
    /**
     * Property tag for the active view.
     */
    public static final String PROP_ACTIVEVIEW = "activeView";

    /**
     * Property tag for the login status.
     */
    public static final String PROP_LOGGEDIN = "loggedIn";

    /**
     * Property tag for the status text.
     */
    public static final String PROP_STATUSTEXT = "statusText";

    public static final String PROP_TERMINATECHATSERVER = "terminateChatServer";

    private static final String TITLE;

    static
    {
        TITLE = String.format("%1$s - Client", PROJECT_TITLE);
    }

    /**
     * Second stage of program start.
     * <p>
     * {@link Main#main(java.lang.String[]) Main.main()} is the primary entry
     * point for this application. This is to get around a class
     * loading issue with the JavaFX classes unpacked into the
     * one "Fat Jar".
     *
     * @param args not used.
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    private Views activeView;

    private boolean loggedIn;

    private Stage primaryStage;

    private final transient PropertyChangeSupport propertyChangeSupport;

    private BorderPane rootLayout;

    private String statusText;

    private String userName;

    /**
     * Instantiate a new copy of the App class.
     */
    public App()
    {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.statusText = "";
        this.userName = "";
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Set the value of loggedIn
     *
     * @param loggedIn new value of loggedIn
     */
    public void setLoggedIn(boolean loggedIn)
    {
        if (this.loggedIn != loggedIn)
        {
            boolean oldLoggedIn = this.loggedIn;
            this.loggedIn = loggedIn;
            propertyChangeSupport.firePropertyChange(PROP_LOGGEDIN, oldLoggedIn, loggedIn);
        }
    }

    /**
     * Used to set the owner of a dialog.
     *
     * @return the primary stage
     */
    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    /**
     * Set the statusText
     *
     * @param statusText new value of statusText
     */
    public void setStatusText(String statusText)
    {
        String temp = statusText != null ? statusText : "";

        if (!this.statusText.equals(temp))
        {
            String oldStatusText = this.statusText;
            this.statusText = temp;
            propertyChangeSupport.firePropertyChange(PROP_STATUSTEXT, oldStatusText, temp);
        }
    }

    /**
     * Get the value of userName
     *
     * @return the value of userName
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Set the value of userName
     *
     * @param userName new value of userName
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout()
    {
        try
        {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            ViewController listener = loader.getController();
            listener.setApp(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException ex)
        {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Shows the view inside the root layout.
     *
     * @param view to display
     */
    public void showView(Views view)
    {
        if (this.activeView != view)
        {
            // Notify everyone of pending change
            Views oldActiveView = this.activeView;
            this.activeView = view;
            propertyChangeSupport.firePropertyChange(PROP_ACTIVEVIEW, oldActiveView, activeView);

            try
            {
                // Load new view.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("view/" + view + ".fxml"));
                AnchorPane newView = (AnchorPane) loader.load();

                ViewController controller = loader.getController();
                controller.setApp(this);

                // Set view into the center of root layout.
                rootLayout.setCenter(newView);
                controller.setFocus();
            } catch (IOException ex)
            {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(TITLE);

        initRootLayout();
    }

    /**
     * Initiate the Terminate Chat Server process.
     */
    public void terminateChatServer()
    {
        propertyChangeSupport.firePropertyChange(PROP_TERMINATECHATSERVER, false, true);
    }

}
