import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket socket = new ServerSocket(PORT)) {
            Socket client = socket.accept();
            handleClient(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        List<String> requestLines = new ArrayList<String>();

        String line;

        do {
            line = reader.readLine();
            requestLines.add(line);
        } while (!line.isBlank());
    }
}
