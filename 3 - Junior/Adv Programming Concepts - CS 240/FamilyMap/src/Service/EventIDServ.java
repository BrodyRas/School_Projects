package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Models.AuthToken;
import Models.Event;
import Results.EventResult;

import java.sql.Connection;


public class EventIDServ {
    public EventIDServ() {
        db = new Database();
    }

    Database db;
    EventDAO eDao;

    /**
     * Returns the single Event object with the specified ID
     *
     * @param eventID ID of event to be found
     * @return Event which matches the given ID
     */
    public EventResult findByID(String authToken, String eventID) throws Exception {
        EventResult result = null;

        try {
            Connection conn = db.openConnection();
            eDao = new EventDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken token = aDao.find(authToken);

            if (token == null) {
                db.closeConnection(false);
                return new EventResult("Invalid Auth Token");
            }
            Event event = eDao.find(eventID);
            if (event == null) {
                db.closeConnection(false);
                return new EventResult("Event not found");
            } else if (!event.getDescendant().equals(token.getUserName())) {
                db.closeConnection(false);
                return new EventResult("Invalid Auth Token");
            } else {
                result = new EventResult(event);
                db.closeConnection(true);
            }
        } catch (DataAccessException e) {
            result = new EventResult("Error encountered while finding event");
            db.closeConnection(false);
        }
        if (result.getEvent() == null) result.setMessage("Event not found");
        return result;
    }
}
