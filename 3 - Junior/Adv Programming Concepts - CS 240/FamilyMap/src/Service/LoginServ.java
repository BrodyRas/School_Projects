package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Models.AuthToken;
import Models.User;
import Requests.LoginRequest;
import Results.LoginResult;

import java.sql.Connection;

public class LoginServ {
    public LoginServ() {
    }

    /**
     * Logs in the user and returns an auth token.
     *
     * @param loginRequest contains Strings for the username and password of the requesting user
     * @return AuthToken associate with user
     */
    public LoginResult login(LoginRequest loginRequest) throws Exception {
        LoginResult result = null;
        Database db = new Database();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            User user = uDao.find(loginRequest.getUserName());
            if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
                result = new LoginResult("User not found");
            } else {
                AuthToken auth = new AuthToken(loginRequest.getUserName());
                result = new LoginResult(auth,
                        loginRequest.getUserName(),
                        user.getPersonID());
                AuthTokenDAO aDao = new AuthTokenDAO(conn);
                aDao.insert(auth);
            }

            db.closeConnection(true);
        } catch (DataAccessException e) {
            result = new LoginResult("Error encountered while finding user");
            db.closeConnection(false);
        }
        return result;
    }
}
