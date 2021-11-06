## GUI Client

This application allows the user to perform the functions:

- Create a new account
- Login to an existing account
- Chat to the echo server (Socket Server)

### App

JavaFX App

### Main

Primary entry point for the application.

### ViewController

Common interface for all view controllers.

### Views

Enumeration of all the views to be displayed.

### AboutController

FXML Controller class for the 'About.fxml' file, which is a dialog window.

### BlankController

This is a dummy controller, just to be compatible with the 
App.showView(view) method.

### ChatController

FXML Controller class for the 'Chat.fxml' file, which is a form for on-line 
messaging.

### LoginController

FXML Controller class for the 'Login.fxml' file, which is a form for 
logging into the chat service.

### NewAccountController

FXML Controller class for the 'NewAccount.fxml' file, which is a form for
creating a new user account, and logging into the chat service.

### RootLayoutController

FXML Controller class for the 'RootLayout.fxml' file, which is the main
window of the application.
