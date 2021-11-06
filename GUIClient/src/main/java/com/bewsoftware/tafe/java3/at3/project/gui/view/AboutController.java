/*
 *  File Name:    AboutController.java
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

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import static com.bewsoftware.tafe.java3.at3.project.common.Constants.BUILD_DATE;
import static com.bewsoftware.tafe.java3.at3.project.common.Constants.PROJECT_TITLE;
import static com.bewsoftware.tafe.java3.at3.project.common.Constants.VERSION;
/**
 * FXML Controller class for the 'About.fxml' file, which is a dialog window.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class AboutController
{
    @FXML
    private Label buildDateLabel;

    @FXML
    private Label copyrightLabel;

    @FXML
    private TextArea licenceTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label productLabel;

    @FXML
    private Label verionLabel;

    private static final String LICENCE
            = "    This program is free software: you can redistribute it and/or "
            + "modify it under the terms of the GNU General Public License as "
            + "published by the Free Software Foundation, either version 3 of "
            + "the License, or (at your option) any later version.\n\n"
            + "    This program is distributed in the hope that it will be useful, "
            + "but WITHOUT ANY WARRANTY; without even the implied warranty of "
            + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU "
            + "General Public License for more details.\n\n"
            + "    You should have received a copy of the GNU General Public "
            + "License along with this program.  If not, see\n"
            + "<http://www.gnu.org/licenses/>.";

    private static final String COPYRIGHT_NOTICE
            = "Copyright (c) 2021 Bradley Willcott";

    private static final String DESCRIPTION
            = "This program was developed for my Diploma in Software Development "
            + "at the South Metropolitan TAFE, Rockingham WA.\n\n"
            + "This application provides a Chat facility that requires the user "
            + "to first Login to the server.";

    /**
     * Instantiate a new copy of the AboutController class.
     */
    public AboutController()
    {
        // Currently: NoOp.
    }

    /**
     * Initializes the controller class.
     */
    @FXML
    private void initialize()
    {
        buildDateLabel.setText(BUILD_DATE);
        copyrightLabel.setText(COPYRIGHT_NOTICE);
        licenceTextArea.setText(LICENCE);
        descriptionTextArea.setText(DESCRIPTION);
        productLabel.setText(PROJECT_TITLE);
        verionLabel.setText(VERSION);
    }
}
