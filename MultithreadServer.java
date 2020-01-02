import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultithreadServer extends Thread implements Serializable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private PrintWriter writer;
    private ServerConnect serverConnect = null;
    private DbConnection dbConnection;
    private BufferedReader reader;
    private ObjectOutputStream objectOutputStream;
    private HashMap<Integer, String> records;
    //private OutputStream outputStream;

    public HashMap<Integer, String> getRecords() {
        return records;
    }

    public MultithreadServer(Socket socket) {
        try {
            this.socket = socket;
       //     outputStream = socket.getOutputStream();
            writer = new PrintWriter(socket.getOutputStream(), true);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            serverConnect = (ServerConnect) objectInputStream.readObject();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dbConnection = new DbConnection();
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
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
                if (serverConnect.getLoginType().equals("register")) {
                    boolean emailMatch;
                    String passwordString = new String(serverConnect.getPassword());
                    emailMatch = dbConnection.checkEmailDetail(serverConnect.getRegEmailAddress());
                    if (!emailMatch) {
                        success = dbConnection.pushRecords(serverConnect.getUserName(), serverConnect.getRegEmailAddress(), serverConnect.getGender(), passwordString);
                        if (success) {
                            System.out.println("Register Success");
                            writer.println("registered");
                            //writer.println("registered");
                            //writer.write("registered\n");

                            //writer.close();
                        }
                    } else {
                        System.out.println("Email clash");
                        writer.println("sameEmail");
                    }
                } else if (serverConnect.getLoginType().equals("login")) {
                    String loginPassword = new String(serverConnect.getLoginPassword());
                    found = dbConnection.checkLoginDetails(serverConnect.getLoginEmailAddress(), loginPassword);
                    if (found) {
                        System.out.println("Login Successful");
                        writer.println("found");
                        // ChatAppServer chatAppServer = new ChatAppServer(socket);
                        // String username = chatAppServer.getUserName(reader.readLine());
                        //  writer.write(username);
                    }else
                    {
                        writer.println("notfound");
                    }

                } else if (serverConnect.getLoginType().equals("online")) {
                    try {


                        records = dbConnection.getOnlineUsers();
                        System.out.println(serverConnect.getLoginEmailAddress());
                        System.out.println(serverConnect.getOnlineStatus());
                        dbConnection.changeOnlineStatus(serverConnect.getLoginEmailAddress(), serverConnect.getOnlineStatus());
                        System.out.println(Arrays.asList(records));
                        objectOutputStream.writeObject(records);
                    } catch (SQLException e) {
                        System.out.println("Error in sqlException under  HashMap<Integer,String> records= dbConnection.getOnlineUsers() function " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           System.out.println(e.getMessage());
        }
    }
}

