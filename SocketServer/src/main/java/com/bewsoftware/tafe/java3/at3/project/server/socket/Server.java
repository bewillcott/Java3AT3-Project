/*
 *  File Name:    Server.java
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
 * Date: 10 Oct 2021
 * ****************************************************************
 */

package com.bewsoftware.tafe.java3.at3.project.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.bewsoftware.tafe.java3.at3.project.common.Constants.*;

/**
 * The Socket Server handles the back-end side of the Chat echo service.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class Server
{
    private static final String TITLE;

    static
    {
        TITLE = String.format("%1$s%2$s - Socket Server (%3$s)", TITLE_INDENT, PROJECT_TITLE, VERSION);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT))
        {
            log("\n%1$s", DOUBLE_LINE);
            log(TITLE);
            log("%1$s\n", DOUBLE_LINE);
            log("Server is listening on port #%1$d", serverSocket.getLocalPort());
            log("%1$s\n", LINE);

            boolean serverAlive = true;
            int sessionNumber = 0;

            while (serverAlive)
            {

                try (Socket clientSocket = serverSocket.accept())
                { // wait, listen and accept connection
                    sessionNumber++;
                    String clientHostName = clientSocket.getInetAddress().getHostName(); // client name
                    int clientPortNumber = clientSocket.getLocalPort(); // port used

                    log("[%1$d] Connected from %2$s on #%3$d", sessionNumber, clientHostName, clientPortNumber);

                    try (
                            // input stream from client
                            BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            // output stream to client
                            PrintStream outStream = new PrintStream​(clientSocket.getOutputStream(), true);)
                    {
                        boolean sessionOpen = true;

                        // Notify client of connection success.
                        String msg = "[" + sessionNumber + "] You have connect to Chat server " + VERSION + "";
                        outStream.println(msg); // send a message to client

                        while (sessionOpen)
                        { // chatting loop
                            String inLine = inStream.readLine(); // read a line from client
                            log("[%1$d] Received from client: %2$s", sessionNumber, inLine);

                            if (inLine == null)
                            {
                                log("[%1$d] Client disconnected uncleanly!", sessionNumber);
                                log("%1$s\n", LINE);
                                sessionOpen = false;
                            } else
                            {

                                switch (inLine)
                                {
                                    case DISCONNECT_SESSION ->
                                    {
                                        log("[%1$d] Client closed session.", sessionNumber);
                                        log("%1$s\n", LINE);
                                        sessionOpen = false;
                                    }

                                    case TERMINATE_SERVER ->
                                    {
                                        log("[%1$d] Client Terminated Server!!!", sessionNumber);
                                        log("%1$s\n", DOUBLE_LINE);
                                        sessionOpen = false;
                                        serverAlive = false;
                                        // disconnect
                                    }

                                    default ->
                                    {
                                    }
                                }
                                // if the client sends disconnect string, then stop
                                // if the client sends terminate server string, then shutdown

                                // Reply to client
                                String outLine = "[" + sessionNumber + "] You said '" + inLine + "'";
                                outStream.println(outLine); // send a message to client
                            }
                        }
                    }
                }
            }
        } catch (IOException e)
        {
            log("IOException occurred: \n%1$s", e);
        }
    }
}
