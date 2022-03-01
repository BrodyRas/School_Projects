package Service;

import DAO.*;
import Models.AuthToken;
import Models.Person;
import Models.User;
import Results.PersonResult;
import Results.UserResult;

import java.sql.Connection;

public class UserServ {
    public UserServ() {
        db = new Database();
    }

    private Database db;

    /**
     * Returns the single Person object with the specified ID
     *
     * @param userName ID of person to be found
     * @return person associated with given PersonID
     */
    public UserResult findByID(String authToken, String userName) throws Exception {
        UserResult result = null;

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken token = aDao.find(authToken);

            //prevents nullPointerException
            if (token == null) {
                db.closeConnection(false);
                return new UserResult("Invalid Auth Token");
            }
            User user = uDao.find(userName);
            if (user == null) {
                db.closeConnection(false);
                return new UserResult("User not found");
            } else if (!user.getUserName().equals(token.getUserName())) {
                db.closeConnection(false);
                return new UserResult("Invalid Auth Token");
            } else {
                result = new UserResult(user);
                db.closeConnection(true);
            }
        } catch (DataAccessException e) {
            result = new UserResult("Error encountered while finding user");
            db.closeConnection(false);
        }
        if (result.getUser() == null) result.setMessage("User not found");
        return result;
    }
}
