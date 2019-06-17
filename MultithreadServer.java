import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MultithreadServer extends Thread implements Serializable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ServerConnect serverConnect = null;

    public MultithreadServer(Socket socket){
        this.socket = socket;

    }

    @Override
    public void run()
    {
        try{
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            serverConnect = (ServerConnect) objectInputStream.readObject();
            System.out.println(serverConnect.getGender());
            System.out.println(serverConnect.getPassword());
            System.out.println(serverConnect.getRegEmailAddress());
            System.out.println(serverConnect.getUserName());

            DbConnection dbConnection = new DbConnection();
            dbConnection.pushRecords(serverConnect.getUserName(),serverConnect.getRegEmailAddress(),serverConnect.getGender(),serverConnect.getPassword());

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
