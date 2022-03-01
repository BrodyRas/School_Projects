package Service;

import DAO.*;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import Results.GeneralResult;

import java.sql.Connection;

public class LoadServ {
    public LoadServ() {
        db = new Database();
    }

    private Database db;

    /**
     * Clears all data from the database, and then loads the posted user, person, and event data into database
     *
     * @param loadRequest contains arrays of users, persons, and events to be added to database
     * @return Message announcing the successful addition of persons and events, or an error message
     */
    public GeneralResult load(LoadRequest loadRequest) throws Exception {
        ClearServ clearServ = new ClearServ();
        clearServ.clear();
        GeneralResult result = null;

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);

            for (User u : loadRequest.getUsers()) {
                uDao.insert(u);
            }
            for (Person p : loadRequest.getPersons()) {
                pDao.insert(p);
            }
            for (Event e : loadRequest.getEvents()) {
                eDao.insert(e);
            }

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            result = new GeneralResult("Error encountered while loading requested information");
            db.closeConnection(false);
        }

        if (result == null) {
            int userSize = loadRequest.getUsers().length;
            int personSize = loadRequest.getPersons().length;
            int eventSize = loadRequest.getEvents().length;
            result = new GeneralResult("Successfully added " + userSize + " Users, " + personSize + " Persons, and " + eventSize + " Events to database");
        }
        return result;
    }
}
