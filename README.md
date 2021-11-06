
# Java3 AT3 Project
This is a TAFE assignment for the Diploma in Software Development,
at the South Metropolitan TAFE, Rockingham, Western Australia.

This project contains a GUI application that has a chat function. 
To access this function, the user needs to log into the Chat Service.
The function for this, and for creating new user accounts, is also
provided.

In the back-end, are two servers.  One (using RMI) provides the login
functionality.  This server uses PBKDF2 cryptographic hashing of the
passwords, which are subsequently stored in a CSV file - local to the
server.

The second server (using Sockets) provides the echo chat functionality.

## Implementation

This project consists of four sub-projects within the aggregator project:

- [Common][c]
- [GUIClient][g]
- [RMIServer][r]
- [SocketServer][s]

### Common Library

This is a non-executable jar, containing a collection of classes that are in 
'common' use across multiple projects.

### GUI Client

This application allows the user to perform the following functions:

- Create a new account
- Login to an existing account
- Chat to the echo server (Socket Server)

### RMI Server

This server application provides the user account functionality as follows:

- Create new accounts
- Validate login requests
- Store user account details in a CSV file
    - Username
    - Password: hashed

### Socket Server

This server application provides an echo chat functionality.

[c]:https://github.com/bewillcott/Java3AT3-Project/tree/master/Common
[g]:https://github.com/bewillcott/Java3AT3-Project/tree/master/GUIClient
[r]:https://github.com/bewillcott/Java3AT3-Project/tree/master/RMIServer
[s]:https://github.com/bewillcott/Java3AT3-Project/tree/master/SocketServer
