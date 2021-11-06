/*
 *  File Name:    NewAccountController.java
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
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.bewsoftware.tafe.java3.at3.project.gui.Views.BLANK;
import static com.bewsoftware.tafe.java3.at3.project.gui.Views.NEW_ACCOUNT;
import static common.UserAccount.RMI_NAME;
import static java.util.regex.Pattern.compile;

/**
 * FXML Controller class for the 'NewAccount.fxml' file, which is a form for
 * creating a new user account, and logging into the chat service.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class NewAccountController implements ViewController
{

    /**
     * Used to validate a new password - pattern 1.
     */
    private static final Pattern PASSWORD_PATTERN_1;

    private static final String PASSWORD_PATTERN_1_STRING = "^.*[A-Z]+.*.*$";

    /**
     * Used to validate a new password - pattern 2.
     */
    private static final Pattern PASSWORD_PATTERN_2;

    private static final String PASSWORD_PATTERN_2_STRING = "^.*[0-9]+.*[0-9]+.*$";

    /**
     * Used to validate a new username - pattern 1.
     */
    private static final Pattern USERNAME_PATTERN_1;

    private static final String USERNAME_PATTERN_1_STRING = "^[a-zA-Z]{1}.+$";

    /**
     * Used to validate a new username - pattern 2.
     */
    private static final Pattern USERNAME_PATTERN_2;

    private static final String USERNAME_PATTERN_2_STRING = "^[a-zA-Z]{1}[a-zA-Z0-9]+$";

    static
    {
        USERNAME_PATTERN_1 = Pattern.compile(USERNAME_PATTERN_1_STRING);
        USERNAME_PATTERN_2 = Pattern.compile(USERNAME_PATTERN_2_STRING);
        PASSWORD_PATTERN_1 = compile(PASSWORD_PATTERN_1_STRING);
        PASSWORD_PATTERN_2 = compile(PASSWORD_PATTERN_2_STRING);
    }

    private App app;

    @FXML
    private Label confirmPasswordLabel;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private PasswordField firstPasswordField;

    @FXML
    private Button resetButton;

    @FXML
    private PasswordField secondPasswordField;

    @FXML
    private Button submitButton;

    @FXML
    private TextField usernameTextField;

    /**
     * Instantiate a new copy of NewAccountController class.
     */
    public NewAccountController()
    {
        // NoOP
    }

    @Override
    public void setApp(App app)
    {
        this.app = app;
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
                if ((Views) evt.getOldValue() == NEW_ACCOUNT)
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
        firstPasswordField.clear();
        secondPasswordField.clear();
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
        if (validUsername(usernameTextField.getText())
                && validPassword(firstPasswordField.getText(), secondPasswordField.getText()))
        {
            try
            {
                Registry registry = LocateRegistry.getRegistry();
                UserAccount userAccount = (UserAccount) registry.lookup(RMI_NAME);

                if (userAccount.create(usernameTextField.getText(), firstPasswordField.getText()))
                {
                    // Login successful!
                    app.setStatusText("New account created! You are logged in.");
                    app.setLoggedIn(true);
                    app.setUserName(usernameTextField.getText());
                    app.showView(BLANK);
                } else
                {
                    // Login FAILED!
                    app.setStatusText("New account creation FAILED! Try again.");
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
        }

        event.consume();
    }

    /**
     * Verify that the password fulfills the basic requirements.
     * <p>
     * Minimum of 1 character in length
     *
     * @param password1 text to verify
     * @param password2 text to verify
     *
     * @return {@code true} if valid
     */
    private boolean validPassword(String password1, String password2)
    {
        boolean rtn = false;

        if (password1 != null && password1.length() >= 1)
        {
            if (password1.equals(password2))
            {
                rtn = true;
            } else
            {
                app.setStatusText("Passwords must be the same.");
                secondPasswordField.requestFocus();
            }
        } else
        {
            app.setStatusText("Must be at least 1 character long.");
            firstPasswordField.requestFocus();
        }

        return rtn;
    }

    /**
     * Verify that the username fulfills the basic requirements.
     * <p>
     * Minimum of 1 character in length
     *
     * @param username text to verify
     *
     * @return {@code true} if valid
     */
    private boolean validUsername(String username)
    {
        boolean rtn = false;

        if (username != null && username.length() >= 1)
        {
            rtn = true;
        } else
        {
            app.setStatusText("Must be at least 1 character long.");
            usernameTextField.requestFocus();
        }

        return rtn;
    }
}
