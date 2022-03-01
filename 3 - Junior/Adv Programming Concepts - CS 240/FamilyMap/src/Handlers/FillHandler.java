package Handlers;

import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import Requests.FillRequest;
import Results.GeneralResult;
import Service.FillServ;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                String path = exchange.getRequestURI().toString();
                String[] splitArray = path.split("/");
                String username = splitArray[2];
                int genCount;
                if (splitArray.length == 4) genCount = Integer.parseInt(splitArray[3]);
                else genCount = 4;

                //Delete existing data
                Database db = new Database();
                Connection conn = db.openConnection();
                PersonDAO pDao = new PersonDAO(conn);
                EventDAO eDao = new EventDAO(conn);
                pDao.deleteAssociatedPersons(username);
                eDao.deleteAssociatedEvents(username);
                db.closeConnection(true);

                //Execute logic
                FillServ fillServ = new FillServ();
                Gson gson = new Gson();
                FillRequest fillRequest = new FillRequest(username, genCount);
                GeneralResult result = fillServ.fillGenerations(fillRequest);
                String resultString = gson.toJson(result);

                //Write response
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
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
