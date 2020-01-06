//import java.io.*;
//import java.net.Socket;
//import java.sql.SQLException;
//
//public class ChatAppServer {
//
//    private ObjectInputStream objectInputStream;
//    private ObjectOutputStream objectOutputStream;
//    private BufferedReader bufferedReader;
//    private PrintWriter printWriter;
//    private Socket socket;
//    private String userEmail;
//    private DbConnection dbConnection;
//
//    public ChatAppServer(Socket socket) throws IOException,SQLException {
//        objectInputStream = new ObjectInputStream(socket.getInputStream());
//        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        printWriter = new PrintWriter(socket.getOutputStream(), true);
//        this.socket = socket;
//        this.dbConnection = new DbConnection();
//    }
//
//    public String getUserName(String email) {
//        try {
//            String username = dbConnection.getUserName(email);
//            printWriter.write(username);
//
//        }catch (SQLException e)
//        {
//            System.out.println("SQL ERROR");
//        }
//    return null;
//    }
//}
//
//
//
  //System.out.println("MESSAGE INFO:\n\tSent From ="+sentFrom+"\n\tSent To ="+sendTo+"\n\tmessage ="+messages);
