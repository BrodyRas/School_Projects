package Service;

import DAO.*;
import Models.AuthToken;
import Models.Event;
import Results.EventResult;

import java.sql.Connection;

public class EventServ {
    public EventServ() {
        db = new Database();
    }

    private Database db;

    /**
     * Returns all events for all family members of the current user
     *
     * @param authToken token of user whose events will be serialized
     * @return an array of all events associated with all of the user's persons
     */
    public EventResult getEvents(String authToken) throws Exception {
        EventResult result = null;

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken token = aDao.find(authToken);

            if (token == null) {
                db.closeConnection(false);
                return new EventResult("Invalid Auth Token");
            } else {
                Event[] events = eDao.associatedEvents(uDao.find(token.getUserName()));
                result = new EventResult(events);
                db.closeConnection(true);
            }
        } catch (DataAccessException e) {
            result = new EventResult("Error encountered while finding associated events");
            db.closeConnection(false);
        }
        return result;
    }
}
