package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Models.AuthToken;
import Models.User;
import Requests.LoginRequest;
import Results.LoginResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;

public class LoginServTest {
    Database db;
    private User UserA, UserB;
    private AuthToken authToken;
    LoginResult ResultA, ResultB;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        db.createTables();
        UserA = new User(
                "brodyr",
                "brody779",
                "brodyras@gmail.com",
                "Brody",
                "Rasmussen",
                "m",
                "1234567890ab"
        );
        UserB = new User(
                "jennyc",
                "ilovebrody",
                "jenchristensen@gmail.com",
                "Jennifer",
                "Christensen",
                "f",
                "0987654321ba"
        );

        authToken = new AuthToken("brodyr", "12345");

        ResultA = new LoginResult(authToken, UserA.getUserName(), UserA.getPersonID());
        ResultB = new LoginResult("User not found");
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void loginPass() throws Exception {
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(UserA);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        LoginRequest loginRequest = new LoginRequest(UserA.getUserName(), UserA.getPassword());
        LoginServ loginServ = new LoginServ();
        LoginResult loginResult = loginServ.login(loginRequest);

        assertEquals(loginResult.getPersonID(), ResultA.getPersonID());
        assertEquals(loginResult.getUserName(), ResultA.getUserName());
    }

    @Test
    public void loginFail() throws Exception {
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(UserB);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        LoginRequest loginRequest = new LoginRequest(UserA.getUserName(), UserA.getPassword());
        LoginServ loginServ = new LoginServ();
        LoginResult loginResult = loginServ.login(loginRequest);

        assertEquals(loginResult, ResultB);
    }
}