package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements HttpHandler {
    Path filePath;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestURI = exchange.getRequestURI().toString();
        String fileStr;
        try {
            if (requestURI.equals("/") || requestURI.equals("index.html")) {
                fileStr = "web/index.html";
            } else {
                fileStr = "web/" + requestURI;
            }
            File file = FileSystems.getDefault().getPath(fileStr).toFile();
            if (!file.exists()) {
                fileStr = "web/HTML/404.html";
            }

            filePath = FileSystems.getDefault().getPath(fileStr);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
