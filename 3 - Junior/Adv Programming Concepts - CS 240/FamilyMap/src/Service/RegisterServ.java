package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import Models.User;
import Requests.FillRequest;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.GeneralResult;
import Results.LoginResult;
import Results.RegisterResult;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;

public class RegisterServ {
    public RegisterServ() {
        db = new Database();
    }

    Database db;
    UserDAO uDao;
    FillServ fillServ;


    private String createID() {
        return RandomStringUtils.random(24, "0123456789abcdef");
    }

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, and logs the user in
     *
     * @param registerRequest the user to be added to Users table
     * @return AuthToken associated with newly registered user
     */
    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        try {
            Connection conn = db.openConnection();
            uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);

            User user = registerRequest.getUser();
            user.setPersonID(createID());

            if (pDao.find(user.getPersonID()) == null) {
                uDao.insert(registerRequest.getUser());
                db.closeConnection(true);
            } else {
                db.closeConnection(false);
                return new RegisterResult("Non-unique personID");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new RegisterResult("User already exists");
        }

        fillServ = new FillServ();
        FillRequest fillRequest = new FillRequest(
                registerRequest.getUser().getUserName(),
                4);

        GeneralResult fillResult = fillServ.fillGenerations(fillRequest);
        if (fillResult.getMessage().charAt(0) == 'N') {
            return new RegisterResult(fillResult.getMessage());
        }

        //logs the user in, and returns an auth token.
        LoginServ loginServ = new LoginServ();
        LoginRequest loginRequest = new LoginRequest(
                registerRequest.getUser().getUserName(),
                registerRequest.getUser().getPassword());
        LoginResult loginResult = loginServ.login(loginRequest);

        return new RegisterResult(loginResult.getAuthToken(),
                registerRequest.getUser().getUserName(),
                registerRequest.getUser().getPersonID());
    }
}
