package Service;

import DAO.*;
import Models.AuthToken;
import Results.PersonResult;

import java.sql.Connection;

public class PersonServ {
    public PersonServ() {
        db = new Database();
    }

    Database db;
    PersonDAO pDao;
    UserDAO uDao;

    /**
     * Returns ALL family members of the current user.
     *
     * @param authToken used to identify which user's persons are to be loaded
     * @return an array all persons associated with the user
     */
    public PersonResult getPersons(String authToken) throws Exception {
        PersonResult result = null;

        try {
            Connection conn = db.openConnection();
            pDao = new PersonDAO(conn);
            uDao = new UserDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken token = aDao.find(authToken);

            if (token == null) {
                db.closeConnection(false);
                return new PersonResult("Invalid Auth Token");
            } else {
                result = new PersonResult(pDao.associatedPersons(uDao.find(token.getUserName())));
                db.closeConnection(true);
            }
        } catch (DataAccessException e) {
            result = new PersonResult("Error encountered while finding associated persons");
            db.closeConnection(false);
        }
        return result;

    }
}
