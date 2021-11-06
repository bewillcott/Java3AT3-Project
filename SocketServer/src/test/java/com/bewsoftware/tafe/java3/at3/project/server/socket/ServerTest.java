/*
 *  File Name:    ServerTest.java
 *  Project Name: SocketServer
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

package com.bewsoftware.tafe.java3.at3.project.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import org.junit.jupiter.api.*;

import static com.bewsoftware.tafe.java3.at3.project.common.Constants.DISCONNECT_SESSION;
import static com.bewsoftware.tafe.java3.at3.project.common.Constants.SERVER_PORT;
import static com.bewsoftware.tafe.java3.at3.project.common.Constants.log;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class ServerTest
{
    private static final String SERVER_NAME = "localhost";

    private static final String[] CHAT_STRINGS =
    {
        "Hi there!", "How are you?", "How is the family?", "When can we meet?"
    };

    private static Thread worker;

    public ServerTest()
    {
    }

    @BeforeAll
    public static void setUpClass()
    {
        worker = new Thread(() ->
        {
            Server.main(null);
        });

        worker.start();
    }

    @AfterAll
    public static void tearDownClass()
    {
        worker.interrupt();
        worker = null;
    }

    @BeforeEach
    public void setUp()
    {
    }

    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Test of main method, of class Server.
     */
    @Test
    public void testChat()
    {
        try
        {
            log("| Test Chat");
            log("| create socket for connection");
            Socket socket = new Socket(SERVER_NAME, SERVER_PORT);

            // get input/output streams
            BufferedReader incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream​ outgoing = new PrintStream​(socket.getOutputStream(), true);

            String line = incoming.readLine();
            log("> %1$s", line);
            assertTrue(line.endsWith("You have connect to Chat server v1.0"));

            for (String text : CHAT_STRINGS)
            {
                // Send text to Socket Server
                outgoing.println(text);

                // Receive incoming reply from Socket Server
                line = incoming.readLine();
                log("> %1$s", line);
                assertTrue(line.endsWith("You said '" + text + "'"));
            }

            // End Chat session
            outgoing.println(DISCONNECT_SESSION);
        } catch (IOException ex)
        {
            fail(ex);
        }
    }
}
