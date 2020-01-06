import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MultithreadServer extends Thread implements Serializable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private PrintWriter writer;
    private ServerConnect serverConnect = null;
    private DbConnection dbConnection;
    private BufferedReader reader;
    private ObjectOutputStream objectOutputStream;
    private HashMap<String, String> records;
    private HashMap<String, String> senderReceivermessageInfo;
    private HashMap<String, String> messageInfo;
    private String messages;
    private String sendTo;
    private String sentFrom;
    private String sendToGmail;
    private HashMap<HashMap,String> sentFromTOMessage;
    //private OutputStream outputStream;

    public HashMap<String, String> getRecords() {
        return records;
    }

    public MultithreadServer(Socket socket) {
        try {
            this.socket = socket;
       //     outputStream = socket.getOutputStream();
            writer = new PrintWriter(socket.getOutputStream(), true); //writes to client
            objectInputStream = new ObjectInputStream(socket.getInputStream()); // read objects from client ( serverconnect being the object, uses serializable interface)
            serverConnect = (ServerConnect) objectInputStream.readObject(); //------------have used serverConnect object from ChatApp client to break down the object data ------
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // reads from client
            dbConnection = new DbConnection(); // creating a database connection object.
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); // passes object to client ( only hashmaps here )
        } catch (IOException e) {

            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        boolean success; // for register success
        boolean found;  // for login, record found or not found
        try {
            {
                if (serverConnect.getLoginType().equals("register")) //this is received from the serverconnect object. The logintype variable in the object specifies what to do.
                                                                    // here being reigster, the server knows its for registering purpose and works accordingly
                {
                    boolean emailMatch; // to check it the email is already used or not.
                    String passwordString = new String(serverConnect.getPassword());  //---------- SUBJECT TO CHANGE --------- Note to self: learn how to hash passwords and where to hash and send
                                                                                      //                                                        in client side? or here?
                    emailMatch = dbConnection.checkEmailDetail(serverConnect.getRegEmailAddress()); // gets true or false, if true send the client a email clash message which then shows in the UI about error same message
                    if (!emailMatch) // if email is unique
                    {
                        success = dbConnection.pushRecords(serverConnect.getUserName(), serverConnect.getRegEmailAddress(), serverConnect.getGender(), passwordString); // push the data to database to store data. Returns true if everything works
                        if (success) {
                            System.out.println("Register Success");
                            writer.println("registered"); // pass this to client. The serverconnect class object receives this message and says the loginPage object to throw registered success message
                            //writer.println("registered");
                            //writer.write("registered\n");

                            //writer.close();
                        }
                    } else {
                        System.out.println("Email clash"); // if same email
                        writer.println("sameEmail"); // send this to client. This is to tell the  serverconnect object that the email is taken which then says the loginpage to throw same email message.
                    }

                } else if (serverConnect.getLoginType().equals("login")) // for login purpose

                {
                    String loginPassword = new String(serverConnect.getLoginPassword());
                    found = dbConnection.checkLoginDetails(serverConnect.getLoginEmailAddress(), loginPassword); // see in database if login details exist or not.
                    if (found) {
                        System.out.println("Login Successful");
                        writer.println("found"); // write this message to client. The serverconnect object will then know that login detail was found and
                                                // forwards this to LoginPage which will close the login frame and start the chatapp frame


                        // ChatAppServer chatAppServer = new ChatAppServer(socket);
                        // String username = chatAppServer.getUserName(reader.readLine());
                        //  writer.write(username);
                    }else
                    {
                        writer.println("notfound"); // write this message to client. The serverconnect object will know the login detail did not match
                                                    // It then tells the LoginPage to throw "email or password mismatch" message.
                    }

                } else if (serverConnect.getLoginType().equals("online"))  // to make the current user online and also to send all online users to client.
                {
                    try {
                        records = dbConnection.getOnlineUsers(); //gets all online user at that time
                        System.out.println("Online user= "+serverConnect.getLoginEmailAddress());
                        //System.out.println(serverConnect.getOnlineStatus());
                        dbConnection.changeOnlineStatus(serverConnect.getLoginEmailAddress(), serverConnect.getOnlineStatus());// this sets the current user who just logged in to online.
                                                                                                                                // Who the user is given by serverconnect.getLoginEmailAddress() func
                        System.out.println(Arrays.asList(records));
                        objectOutputStream.writeObject(records); //sends the online user hashmap record object to client.
                    } catch (SQLException e) {
                        System.out.println("Error in sqlException under  HashMap<Integer,String> records= dbConnection.getOnlineUsers() function " + e.getMessage());
                    }
                }else if (serverConnect.getLoginType().equals("message")) // for passing messages to and from client
                {
                    System.out.println("\nReceived message");
                       /*

                     THIS MIGHT BE THE PLACE TO DO store message in database maybe ??
                     NOTE TO SELF: Think about this later on.


                     */
                    senderReceivermessageInfo = serverConnect.getSendMessagesHashMap(); // receives the message into. Contains who sent to whom ( key is in email value is in username)
                    for (String value : senderReceivermessageInfo.values()) {
                        this.sendTo = value; // to whom was it sent
                    }
                    for (String value : senderReceivermessageInfo.keySet()) {
                        this.sentFrom = value;  // who sent
                    }
                    messages = serverConnect.getMessage(); // what was the message sent
                    sendToGmail=dbConnection.getUserEmail(sendTo); // get the email address of the person to whom the message was sent
                    System.out.println("MESSAGE INFO:\n\tSent From ="+sentFrom+"\n\tSent To ="+sendTo+"\n\tmessage ="+messages); //displaying the message info
                    messageInfo=new HashMap<>();
                    sentFromTOMessage = new HashMap<>(); /* ------------------------- SUBJECT TO CHANGE -------------------
                                                                            Unnecessary use of data structure object containing the same info.
                                                                            --------------------------------------*/
                    messageInfo.put(sentFrom,sendToGmail);  // did this to make it easier for serverconnect class in chatapp project to make it easier to know who sent to whom as email is always unique.

                    objectOutputStream.writeObject(messageInfo); // passes the email address. ------------SUBJECT TO CHANGE--------------

                    writer.println(messages);// forward the message. --------------------- SUBJECT TO CHANGE-------------------
                                            //                             how to bind the message to sender and receiver?
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           System.out.println(e.getMessage());
        }
    }
}

