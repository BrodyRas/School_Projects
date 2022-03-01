package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Models.AuthToken;
import Models.Person;
import Results.PersonResult;

import java.sql.Connection;

public class PersonIDServ {
    public PersonIDServ() {
        db = new Database();
    }

    private Database db;

    /**
     * Returns the single Person object with the specified ID
     *
     * @param PersonID ID of person to be found
     * @return person associated with given PersonID
     */
    public PersonResult findByID(String authToken, String PersonID) throws Exception {
        PersonResult result = null;

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken token = aDao.find(authToken);

            //prevents nullPointerException
            if (token == null) {
                db.closeConnection(false);
                return new PersonResult("Invalid Auth Token");
            }
            Person person = pDao.find(PersonID);
            if (person == null) {
                db.closeConnection(false);
                return new PersonResult("Person not found");
            } else if (!person.getDescendant().equals(token.getUserName())) {
                db.closeConnection(false);
                return new PersonResult("Invalid Auth Token");
            } else {
                result = new PersonResult(pDao.find(PersonID));
                db.closeConnection(true);
            }
        } catch (DataAccessException e) {
            result = new PersonResult("Error encountered while finding person");
            db.closeConnection(false);
        }
        if (result.getPerson() == null) result.setMessage("Person not found");
        return result;
    }
}
