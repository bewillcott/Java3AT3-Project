/*
 *  File Name:    Server.java
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
 * Date: 9 Oct 2021
 * ****************************************************************
 */

package com.bewsoftware.tafe.java3.at3.project.server.rmi;

import com.bewsoftware.tafe.java3.at3.project.common.AvlTree;
import com.bewsoftware.tafe.java3.at3.project.common.CSVFile;
import com.bewsoftware.tafe.java3.at3.project.common.CSVRow;
import com.bewsoftware.tafe.java3.at3.project.common.PBKDF2;
import com.bewsoftware.tafe.java3.at3.project.common.PBKDF2.CannotPerformOperationException;
import com.bewsoftware.tafe.java3.at3.project.common.PBKDF2.InvalidHashException;
import common.UserAccount;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.bewsoftware.tafe.java3.at3.project.common.Constants.*;

/**
 * The RMI Server handles the back-end side of the User Login process.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class Server implements UserAccount
{
    private static final String TITLE;

    static
    {
        TITLE = String.format("%1$s%2$s - RMI Server (%3$s)", TITLE_INDENT, PROJECT_TITLE, VERSION);
    }

    /**
     * CSV file header text.
     */
    private static final String[] HEADER =
    {
        "Username", "PasswordHash"
    };

    /**
     * File to persist user account details.
     */
    static final String DATASTORE = "user_accounts.csv";

    static Registry registry;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        log("\n%1$s", DOUBLE_LINE);
        log(TITLE);
        log("%1$s\n", DOUBLE_LINE);

        try
        {
            UserAccount engine = new Server();
            UserAccount stub = (UserAccount) UnicastRemoteObject.exportObject(engine, 0);

            registry = LocateRegistry.createRegistry(1099);
            registry.bind(RMI_NAME, stub);
            log("Server bound\n%1$s\n", LINE);
        } catch (IOException | AlreadyBoundException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The data from/for the CSV file.
     */
    private final CSVFile userCSVFile;

    /**
     * The sorted list of registered users.
     */
    private final AvlTree<User> users;

    /**
     * Instantiate a new copy of the Server class.
     *
     * @throws java.io.IOException if any
     */
    public Server() throws IOException
    {
        users = new AvlTree<>();
        userCSVFile = new CSVFile(DATASTORE);

        if (userCSVFile.readData(true))
        {
            for (CSVRow row : userCSVFile)
            {
                users.add(new User(row.get(0), row.get(1)));
            }
        } else
        {
            userCSVFile.setHeader(HEADER);
        }
    }

    @Override
    public boolean create(String username, String password) throws RemoteException
    {
        boolean rtn = false;
        User user = new User(username, null);

        if (!users.contains(user))
        {
            try
            {
                user.passwordHash = PBKDF2.createHash(password);
                users.add(user);

                if (userCSVFile.add(CSVRow.parse(user.username, user.passwordHash)))
                {
                    userCSVFile.writeData();
                }

                rtn = true;
            } catch (CannotPerformOperationException | IOException ex)
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (rtn)
        {
            log("New account created for: %1$s.", username);
            log("User (%1$s) is logged in.", username);
        } else
        {
            log("Account creation for: %1$s - FAILED!", username);
        }

        return rtn;
    }

    @Override
    public boolean login(String username, String password) throws RemoteException
    {
        boolean rtn = false;
        User user = new User(username, null);

        if (users.contains(user))
        {
            String pwd = users.get(users.indexOf(user)).passwordHash;

            try
            {
                rtn = PBKDF2.verifyPassword(password, pwd);
            } catch (CannotPerformOperationException | InvalidHashException ex)
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (rtn)
        {
            log("User (%1$s) is logged in.", username);
        } else
        {
            log("User (%1$s) failed to login.", username);
        }

        return rtn;
    }

    /**
     * Holds user account details.
     */
    private static class User implements Comparable<User>
    {
        /**
         * The hashed password.
         */
        public String passwordHash;

        /**
         * The username - key.
         */
        public final String username;

        /**
         * Instantiates a new copy of the User class.
         *
         * @param username     to store
         * @param passwordHash to store
         */
        public User(String username, String passwordHash)
        {
            this.username = username;
            this.passwordHash = passwordHash;
        }

        @Override
        public int compareTo(User o)
        {
            return this.equals(o) ? 0 : this.username.compareTo(o.username);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }

            if (obj == null)
            {
                return false;
            }

            if (getClass() != obj.getClass())
            {
                return false;
            }

            final User other = (User) obj;
            return Objects.equals(this.username, other.username);
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.username);
            return hash;
        }
    }
}
