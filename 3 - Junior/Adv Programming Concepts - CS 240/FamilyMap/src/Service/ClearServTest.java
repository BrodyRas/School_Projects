package Service;

import DAO.*;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNull;

public class ClearServTest {
    private Database db;
    private AuthToken AuthA;
    private Event EventA;
    private Person PersonA;
    private User UserA;


    @Before
    public void setUp() throws Exception {
        db = new Database();
        db.createTables();

        AuthA = new AuthToken("brodyr", "1234567890");

        EventA = new Event("1234567890ab",
                "brodyr",
                "person-ID",
                "USA",
                "Provo",
                112,
                221,
                "Birth",
                1995);

        PersonA = new Person(
                "1234567890ab",
                "brodyr",
                "Brody",
                "Rasmussen",
                "m");

        UserA = new User(
                "brodyr",
                "brody779",
                "brodyras@gmail.com",
                "Brody",
                "Rasmussen",
                "m",
                "1234567890ab"
        );
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void clear() throws Exception {
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            EventDAO eDao = new EventDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);

            aDao.insert(AuthA);
            eDao.insert(EventA);
            pDao.insert(PersonA);
            uDao.insert(UserA);

            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        ClearServ clearServ = new ClearServ();
        clearServ.clear();

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            EventDAO eDao = new EventDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);

            assertNull(aDao.find(AuthA.getToken()));
            assertNull(eDao.find(EventA.getEventID()));
            assertNull(pDao.find(PersonA.getPersonID()));
            assertNull(uDao.find(UserA.getUserName()));

            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}