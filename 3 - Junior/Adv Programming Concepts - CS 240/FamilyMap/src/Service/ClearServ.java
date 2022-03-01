package Service;

import DAO.*;
import Results.GeneralResult;

import java.sql.Connection;

public class ClearServ {
    public ClearServ() {
        db = new Database();
    }

    Database db;
    AuthTokenDAO aDao;
    EventDAO eDao;
    PersonDAO pDao;
    UserDAO uDao;

    /**
     * Deletes all data from the database, including user accounts, auth tokens, and generated person and event data.
     *
     * @return message announcing a successful clear, or an error
     */
    public GeneralResult clear() throws Exception {
        GeneralResult result = null;
        db = new Database();

        try {
            Connection conn = db.openConnection();
            aDao = new AuthTokenDAO(conn);
            eDao = new EventDAO(conn);
            pDao = new PersonDAO(conn);
            uDao = new UserDAO(conn);
            aDao.clear();
            eDao.clear();
            pDao.clear();
            uDao.clear();

            result = new GeneralResult("Clear Succeeded");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            result = new GeneralResult("Error encountered while clearing");
            db.closeConnection(false);
        }
        return result;
    }
}
