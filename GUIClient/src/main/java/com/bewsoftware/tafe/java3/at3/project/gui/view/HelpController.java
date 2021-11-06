/*
 *  File Name:    HelpController.java
 *  Project Name: Java3AT3-Project
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
 * Date: 20 Oct 2021
 * ****************************************************************
 */

package com.bewsoftware.tafe.java3.at3.project.gui.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import static com.bewsoftware.tafe.java3.at3.project.common.Constants.log;

/**
 * FXML Controller class for the 'Help.fxml' file, which is a dialog window.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class HelpController
{
    @FXML
    private WebView webView;

    @FXML
    private void initialize()
    {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                HelpController.class.getResourceAsStream("/help/help.html"))))
        {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
            {
                sb.append(line).append('\n');
            }

            webView.getEngine().loadContent(sb.toString());
        } catch (IOException ex)
        {
            log("%1$s", ex.toString());
        }
    }
}
