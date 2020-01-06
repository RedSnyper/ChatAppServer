# CHATAPP SERVER

This is the sever-side the chatapp project.

Server.java -> accepts connection coming to the port (6000). **Has the main function**

MultithreadServer.java -> makes a new thread for every client connected.\
 Is also responsible for handing all user info(login,reigster) and forwarding all messages.

DbConnection.java -> A database connection class. Used by multithreadServer.java

**The multithreadServer.java uses the ServerConnect class from the ChatApp project.\
 The serverconnect class is needed to be rebuilt everytime a change is done in ServerConnect class\ in ChatApp project.**



