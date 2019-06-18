import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MultithreadServer extends Thread implements Serializable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private PrintWriter writer;
    private ServerConnect serverConnect = null;

    public MultithreadServer(Socket socket){
        this.socket = socket;

    }

    @Override
    public void run()
    {
        boolean success; // for register success
        boolean found;  // for login, record found or not found
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            serverConnect = (ServerConnect) objectInputStream.readObject();
            DbConnection dbConnection = new DbConnection();

            if (serverConnect.getLoginType().equals("register")) {
                boolean emailMatch;
                String passwordString = new String(serverConnect.getPassword());
                emailMatch = dbConnection.checkEmailDetail(serverConnect.getRegEmailAddress());
                if(!emailMatch)
                {
                    success = dbConnection.pushRecords(serverConnect.getUserName(), serverConnect.getRegEmailAddress(), serverConnect.getGender(), passwordString);
                    if (success) {
                        System.out.println("Register Success");
                        writer.println("registered");
                    }
                }else{
                    System.out.println("Email clash");
                    writer.println("sameEmail");
                }
            } else {
                String loginPassword = new String(serverConnect.getLoginPassword());
                found = dbConnection.checkLoginDetails(serverConnect.getLoginEmailAddress(), loginPassword);
                if(found)
                {
                    System.out.println("Login Successful");
                    writer.println("found");
                }

            }
        }catch(IOException e )
        {

            System.out.println(e.getMessage());
        }catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }


//        int threadName = (int) Thread.currentThread().getId();
//        try{
//            writer = new PrintWriter(socket.getOutputStream(),true);
//            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            System.out.println("Client connected in thread" + threadName);
//
//            boolean isTrue = true;
//            while(isTrue) {
//                String message = reader.readLine().toLowerCase();
//                if (!message.equals("close") || !message.equals("exit")) {
//                    System.out.println("Message from client: " +threadName);
//                    System.out.println(message);
//                    writer.println(threadName + " : " + message);
//
//                } else {
//                    System.out.println("Closing connection to client connected on thread" + threadName);
//
//                    writer.close();
//                    reader.close();
//                    socket.close();
//                    isTrue = false;
//                }
//            }
//
//        }catch(IOException e)
//        {
//            System.out.println(e.getMessage());
//        }
    }
}
