import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private static final int PORT = 8080;
    static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    executorService.submit(new ServerHandler(client));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
