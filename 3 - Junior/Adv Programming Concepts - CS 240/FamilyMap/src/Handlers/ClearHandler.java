package Handlers;

import Results.GeneralResult;
import Service.ClearServ;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                // Get the request body input stream
                InputStream reqBody = exchange.getRequestBody();
                InputStreamReader reader = new InputStreamReader(reqBody);

                Gson gson = new Gson();
                ClearServ clearServ = new ClearServ();

                GeneralResult clearResult = clearServ.clear();
                String resultString = gson.toJson(clearResult);
                OutputStream writer = exchange.getResponseBody();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                writeString(resultString, writer);
                exchange.getResponseBody().close();

                success = true;
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}