import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(5000);
        Socket socket = null;

        try {
            while(true) {
                socket = serverSocket.accept();
                MultithreadServer multithreadServer = new MultithreadServer(socket);
                multithreadServer.start();
            }
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e.getMessage());

        } finally {
            serverSocket.close();
            socket.close();
        }

    }
}

