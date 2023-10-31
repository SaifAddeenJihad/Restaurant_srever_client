import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    public static void main(String[] argv) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3333);
        MultiThreadedServer.doSer();


        while (true) {
            Socket so = serverSocket.accept();
            MultiThreadedServer multi= new MultiThreadedServer(so);
            multi.start();

        }
    }
}
