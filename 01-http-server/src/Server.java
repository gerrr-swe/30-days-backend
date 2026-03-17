import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Server {

    private static final int PORT = 8080;
    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private static final String dynamic_path = "/dynamic.html";
    private static final byte[] NOT_FOUND_HTML = "<h1>404: Content not found</h1>".getBytes();

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

        // GET /index.html HTTP/1.1
        String[] requestLine = requestLines.getFirst().split(" ");
        String path = requestLine[1];

        Path filePath = Paths.get("src",path);

        if (dynamic_path.equals(path)) {
            sendResponse(
                    client,
                    "200 ok",
                    "text/html",
                    getDynamicResponse()
            );
        } else if (Files.exists(filePath)) {
            sendResponse(
                    client,
                    "200 ok",
                    Files.probeContentType(filePath),
                    Files.readAllBytes(filePath)
            );
        } else {
            sendResponse(
                    client,
                    "404 Not Found",
                    "text/html",
                    NOT_FOUND_HTML
            );
        }

    }

    private static void sendResponse(
            Socket client,
            String status,
            String contentType,
            byte[] content
    ) throws IOException {
        String LINE_BREAK = "\r\n";
        OutputStream output = client.getOutputStream();
        output.write(("HTTP/1.1 "+status).getBytes());
        output.write(("ContentType: " + contentType + LINE_BREAK).getBytes());
        output.write(LINE_BREAK.getBytes());
        output.write(content);
        output.write((LINE_BREAK + LINE_BREAK).getBytes());
        output.flush();
        client.close();
    }

    private static byte[] getDynamicResponse() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);
        String response = String.format(
                "<h1>Dynamic response</h1><p>Today is %s</p>", dateFormat.format(calendar.getTime())
        );
        return response.getBytes();
    }
}
