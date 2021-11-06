/*
 *  File Name:    LoginController.java
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
import common.UserAccount;
import java.beans.PropertyChangeEvent;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.bewsoftware.tafe.java3.at3.project.gui.Views.BLANK;
import static com.bewsoftware.tafe.java3.at3.project.gui.Views.LOGIN;
import static common.UserAccount.RMI_NAME;

/**
 * FXML Controller class for the 'Login.fxml' file.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class LoginController implements ViewController
{
    private App app;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Button submitButton;

    @FXML
    private PasswordField thePasswordField;

    @FXML
    private TextField usernameTextField;

    /**
     * Instantiate a new copy of NewAccountController class.
     */
    public LoginController()
    {
        // NoOp
    }

    @Override
    public void setApp(App app)
    {
        this.app = app;
        usernameTextField.setText(app.getUserName());
        app.setStatusText("");
        app.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        switch (evt.getPropertyName())
        {
            case App.PROP_ACTIVEVIEW:
            {
                if ((Views) evt.getOldValue() == LOGIN)
                {
                    app.removePropertyChangeListener(this);
                }

                break;
            }

            default:
            {
                break;
            }
        }
    }

    @Override
    public void setFocus()
    {
        usernameTextField.requestFocus();
    }

    /**
     * Handle the Reset Button event.
     *
     * @param event
     */
    @FXML
    private void handleResetButton(ActionEvent event)
    {
        usernameTextField.clear();
        thePasswordField.clear();
        event.consume();
    }

    /**
     * Handle the Submit Button event.
     *
     * @param event
     */
    @FXML
    private void handleSubmitButton(ActionEvent event)
    {
        try
        {
            Registry registry = LocateRegistry.getRegistry();
            UserAccount userAccount = (UserAccount) registry.lookup(RMI_NAME);

            if (userAccount.login(usernameTextField.getText(), thePasswordField.getText()))
            {
                // Login successful!
                app.setStatusText("Login successful!");
                app.setLoggedIn(true);
                app.setUserName(usernameTextField.getText());
                app.showView(BLANK);
            } else
            {
                // Login FAILED!
                app.setStatusText("Login FAILED! Try again.");
                usernameTextField.requestFocus();
            }
        } catch (RemoteException | NotBoundException ex)
        {
            if (ex instanceof ConnectException cause
                    && cause.getMessage().startsWith("Connection refused"))
            {
                app.setStatusText("User Account Server: Connection refused!");
                usernameTextField.requestFocus();
            } else
            {
                Logger.getLogger(NewAccountController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        event.consume();
    }
}
