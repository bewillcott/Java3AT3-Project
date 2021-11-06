/*
 *  File Name:    Constants.java
 *  Project Name: Common
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
 * Date: 11 Oct 2021
 * ****************************************************************
 */

package com.bewsoftware.tafe.java3.at3.project.common;

/**
 * Constants is a utility class containing only common constants.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class Constants
{
    /**
     * Build date of the project.
     */
    public static final String BUILD_DATE = "6 November 2021";

    /**
     * Chat service disconnect string.
     */
    public static final String DISCONNECT_SESSION = "-=quit=-";

    /**
     * Title separator line.
     */
    public static final String DOUBLE_LINE;

    /**
     * Session separator line.
     */
    public static final String LINE;
    /*
     * The Title for the project.
     */
    public static final String PROJECT_TITLE = "Java3 AT3 Project";

    /**
     * RMIServer port number.
     */
    public static final int SERVER_PORT = 9010;

    /**
     * Chat service server terminate string.
     */
    public static final String TERMINATE_SERVER = "!**_Terminate_Server_**!";

    /**
     * Title indent.
     */
    public static final String TITLE_INDENT;

    /**
     * Project version number.
     */
    public static final String VERSION = "v1.0";

    static
    {
        DOUBLE_LINE = "=".repeat(80);
        LINE = "-".repeat(80);
        TITLE_INDENT = " ".repeat(20);
    }

    /**
     * Log messages to the standard console.
     *
     * @implNote
     * Uses {@code System#out#prinf(String, Object...)}.
     *
     * @param message format string
     * @param args    optional arguments
     */
    public static void log(String message, Object... args)
    {
        System.out.printf(message + '\n', args);
    }

    /**
     * Not meant to be instantiated.
     */
    private Constants()
    {
    }
}
