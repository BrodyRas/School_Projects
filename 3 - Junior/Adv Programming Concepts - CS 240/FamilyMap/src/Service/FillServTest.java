package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import Models.Person;
import Models.User;
import Requests.FillRequest;
import Results.GeneralResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FillServTest {
    private Database db;
    private User UserA, UserB;
    private Person PersonA;
    private GeneralResult ResultA, ResultB;


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

        PersonA = new Person(
                "1234567890ab",
                "brodyr",
                "Brody",
                "Rasmussen",
                "m");

        ResultA = new GeneralResult("Successfully added 31 persons and 91 events to database");
        ResultB = new GeneralResult("User not found");
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void fillGenerationsPass() throws Exception {
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(UserA);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        FillRequest fillRequest = new FillRequest(UserA.getUserName(), 4);
        FillServ fillServ = new FillServ();
        GeneralResult fillResult = fillServ.fillGenerations(fillRequest);

        assertEquals(ResultA, fillResult);

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            Person person = pDao.find(UserA.getPersonID());
            db.closeConnection(true);
            assertEquals(PersonA.getPersonID(), person.getPersonID());
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void fillGenerationsFail() throws Exception {
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(UserA);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        FillRequest fillRequest = new FillRequest(UserB.getUserName(), 4);
        FillServ fillServ = new FillServ();
        GeneralResult fillResult = fillServ.fillGenerations(fillRequest);

        assertEquals(ResultB, fillResult);

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            assertNull(pDao.find(UserA.getPersonID()));
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}