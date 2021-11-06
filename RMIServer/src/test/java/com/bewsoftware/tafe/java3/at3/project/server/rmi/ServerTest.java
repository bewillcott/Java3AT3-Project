/*
 *  File Name:    ServerTest.java
 *  Project Name: RMIServer
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

package com.bewsoftware.tafe.java3.at3.project.server.rmi;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 */
public class ServerTest
{

    private static final String[][] USER_ACCOUNTS =
    {
        {
            "fred1234", "Fred1234", "This1sr0ng"
        },
        {
            "mary4321", "Mary4321", "S01sThi6"
        }
    };

    public ServerTest()
    {
    }

    @BeforeAll
    public static void setUpClass() throws IOException
    {
        Server.main(null);

        File file = new File(Server.DATASTORE);

        if (file.exists())
        {
            file.delete();
        }
    }

    @AfterAll
    public static void tearDownClass()
    {
        Server.registry = null;
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
     * Test create and login methods, of class Server.
     */
    @Test
    public void testUserAccounts() throws Exception
    {
        System.out.println("create - succeed");
        Server instance = new Server();

        for (String[] userAccount : USER_ACCOUNTS)
        {
            String username = userAccount[0];
            String password = userAccount[1];
            boolean result = instance.create(username, password);
            assertTrue(result);
        }

        System.out.println("login - succeed");

        for (String[] userAccount : USER_ACCOUNTS)
        {
            String username = userAccount[0];
            String password = userAccount[1];
            boolean result = instance.login(username, password);
            assertTrue(result);
        }

        System.out.println("create - fail");

        for (String[] userAccount : USER_ACCOUNTS)
        {
            String username = userAccount[0];
            String password = userAccount[1];
            boolean result = instance.create(username, password);
            assertFalse(result);
        }

        System.out.println("login - fail");

        for (String[] userAccount : USER_ACCOUNTS)
        {
            String username = userAccount[0];
            String password = userAccount[2];
            boolean result = instance.login(username, password);
            assertFalse(result);
        }
    }
}
