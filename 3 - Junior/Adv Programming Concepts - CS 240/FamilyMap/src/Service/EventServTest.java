package Service;

import DAO.*;
import Models.AuthToken;
import Models.Event;
import Models.User;
import Results.EventResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventServTest {
    private Database db;
    private Event EventA, EventB, EventC, EventD;
    private EventResult ResultA, ResultB, ResultC, ResultD;
    private User UserA;
    private AuthToken authToken;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        EventA = new Event("1234567890ab",
                "brodyr",
                "person-ID",
                "USA",
                "Provo",
                112,
                221,
                "Birth",
                1995);

        EventB = new Event("0987654321ba",
                "brodyr",
                "diff-person-ID",
                "USA",
                "New York",
                332,
                223,
                "Made a friend",
                2000);
        EventC = new Event("1324354657687",
                "brodyr",
                "other-person-ID",
                "Russia",
                "Petersburg",
                987,
                456,
                "Sang in chapel",
                2014);
        EventD = new Event("0897867564534",
                "dodieg",
                "last-person-ID",
                "Mexico",
                "Mexico City",
                456,
                654,
                "MTC",
                2015);
        ResultA = new EventResult(new Event[]{EventA, EventB});
        ResultB = new EventResult(new Event[]{EventB, EventC});
        ResultD = new EventResult("Events not found");

        UserA = new User(
                "brodyr",
                "brody779",
                "brodyras@gmail.com",
                "Brody",
                "Rasmussen",
                "m",
                "1234567890ab"
        );
        authToken = new AuthToken("brodyr", "1234567890");

        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void getEventsPass() throws Exception {
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(authToken);
            uDao.insert(UserA);
            eDao.insert(EventA);
            eDao.insert(EventB);
            eDao.insert(EventD);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        EventServ eventServ = new EventServ();
        EventResult eventResult = eventServ.getEvents(authToken.getToken());
        assertEquals(ResultA, eventResult);
    }

    @Test
    public void getEventsFail() throws Exception {
        Event[] empty = new Event[]{};
        assertTrue(empty.length == 0);
        EventResult emptyResult = new EventResult(empty);

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(authToken);
            uDao.insert(UserA);
            eDao.insert(EventD);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        EventServ eventServ = new EventServ();
        EventResult actualResult = eventServ.getEvents(authToken.getToken());
        assertEquals(emptyResult, actualResult);

    }
}