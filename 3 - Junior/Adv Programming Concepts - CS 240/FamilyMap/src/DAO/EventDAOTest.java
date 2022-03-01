package DAO;

import Models.Event;
import Models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class EventDAOTest {
    private Database db;
    private Event EventA, EventB, EventC, Counterfeit;
    private User UserA;

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

        EventC = new Event("1234567890ba",
                "jennyc",
                "diff-person-ID",
                "USA",
                "New York",
                332,
                223,
                "Met a cat",
                2000);

        Counterfeit = new Event("1234567890ab",
                "brodyr",
                "some-dude",
                "Russia",
                "Petersburg",
                554,
                445,
                "Incarceration",
                2015);

        UserA = new User(
                "brodyr",
                "brody779",
                "brodyras@gmail.com",
                "Brody",
                "Rasmussen",
                "m",
                "1234567890ab"
        );

        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {
        Event check;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            eDao.insert(EventA);
            check = eDao.find(EventA.getEventID());

            db.closeConnection(true);

            assertNotNull(check);
            assertEquals(check, EventA);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            //The counterfeit lacks a unique EventID, which will cause a rollback
            eDao.insert(EventA);
            eDao.insert(Counterfeit);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            didItWork = false;
            db.closeConnection(false);
        }

        assertFalse(didItWork);
        Event check = null;

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            //Ensures that EventA was never added, due to the rollback
            check = eDao.find(EventA.getEventID());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(check);
    }

    @Test
    public void findPass() throws Exception {
        Event check = null;

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            //Proves unique events can be added to and found from the database
            eDao.insert(EventA);
            eDao.insert(EventB);
            check = eDao.find(EventA.getEventID());
            assertEquals(check, EventA);
            check = eDao.find(EventB.getEventID());
            assertEquals(check, EventB);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void findFail() throws Exception {
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            //Ensures that a deleted Event (or an Event never added) cannot be found in the database
            eDao.insert(EventA);
            assertNotNull(eDao.find(EventA.getEventID()));
            eDao.clear();
            assertNull(eDao.find(EventA.getEventID()));
            assertNull(eDao.find(EventB.getEventID()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void associatedPass() throws Exception {
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            //Ensures that associatedEvents will only return events with the proper descendant
            eDao.insert(EventA);
            eDao.insert(EventB);
            eDao.insert(EventC);

            Event[] model = {EventA, EventB};

            Event[] check = eDao.associatedEvents(UserA);
            assertArrayEquals(model, check);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void associatedFail() throws Exception {
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            //Ensures that associatedEvents will return an empty set when queried on an empty database
            Event[] empty = new Event[0];
            assertEquals(0, empty.length);
            eDao.insert(EventA);
            eDao.insert(EventB);
            eDao.clear();
            assertArrayEquals(empty, eDao.associatedEvents(UserA));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void clear() throws Exception {
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            //Ensures database is actually cleared
            eDao.insert(EventA);
            eDao.insert(EventB);
            eDao.insert(EventC);
            eDao.clear();
            assertNull(eDao.find(EventA.getEventID()));
            assertNull(eDao.find(EventB.getEventID()));
            assertNull(eDao.find(EventC.getEventID()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}