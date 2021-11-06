/*
 *  File Name:    module-info.java
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

/**
 * GUIClient module description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
module GUIClient {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires java.rmi;
    requires java.logging;
    requires CommonLibrary;
    requires java.desktop;

    opens com.bewsoftware.tafe.java3.at3.project.gui to javafx.graphics;
    opens com.bewsoftware.tafe.java3.at3.project.gui.view to javafx.fxml, javafx.graphics;
}