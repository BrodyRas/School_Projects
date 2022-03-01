package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Models.AuthToken;
import Models.Event;
import Results.EventResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;


public class EventIDServTest {
    private Database db;
    private Event EventA, EventB;
    private AuthToken authToken;
    private EventResult ResultA, ResultB, ResultC, ResultD;

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
        authToken = new AuthToken("brodyr", "12345");
        ResultA = new EventResult(EventA);
        ResultB = new EventResult(EventB);
        ResultC = new EventResult("Invalid Auth Token");
        ResultD = new EventResult("Event not found");
        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void findByIDPass() throws Exception {
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(authToken);
            eDao.insert(EventA);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        EventIDServ eventIDServ = new EventIDServ();
        EventResult eventResult = eventIDServ.findByID(authToken.getToken(), EventA.getEventID());
        assertEquals(ResultA, eventResult);
    }

    @Test
    public void findByIDFail() throws Exception {
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(authToken);
            eDao.insert(EventA);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Proves the Service doesn't retrieve Events which were never added to the Database
        EventIDServ eventIDServ = new EventIDServ();
        EventResult eventResult = eventIDServ.findByID(authToken.getToken(), EventB.getEventID());
        assertEquals(ResultD, eventResult);

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            eDao.clear();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Proves the Service cannot retrieve cleared Events from the Database
        eventResult = eventIDServ.findByID(authToken.getToken(), EventA.getEventID());
        assertEquals(ResultD, eventResult);
    }
}