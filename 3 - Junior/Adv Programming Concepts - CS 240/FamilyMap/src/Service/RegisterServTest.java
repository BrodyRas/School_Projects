package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import Models.Person;
import Models.User;
import Requests.RegisterRequest;
import Results.RegisterResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RegisterServTest {
    private Database db;
    private User UserA, UserB;
    private RegisterResult ResultA, ResultB;

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
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void registerPass() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(UserA);
        RegisterServ registerServ = new RegisterServ();
        RegisterResult registerResult = registerServ.register(registerRequest);

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);

            User u = uDao.find(UserA.getUserName());
            Person p = pDao.find(UserA.getPersonID());

            db.closeConnection(true);

            assertEquals(registerResult.getPersonID(), p.getPersonID());
            assertEquals(registerResult.getUserName(), u.getUserName());
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void registerFail() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(UserB);
        RegisterServ registerServ = new RegisterServ();
        RegisterResult registerResult = registerServ.register(registerRequest);

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);

            User u = uDao.find(UserA.getUserName());
            Person p = pDao.find(UserA.getPersonID());

            db.closeConnection(true);

            assertNull(u);
            assertNull(p);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}