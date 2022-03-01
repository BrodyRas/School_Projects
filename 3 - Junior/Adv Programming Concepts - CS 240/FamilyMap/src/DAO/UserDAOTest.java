package DAO;

import Models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class UserDAOTest {
    private Database db;
    private User UserA, UserB, Counterfeit;

    @Before
    public void setUp() throws Exception {
        db = new Database();

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

        Counterfeit = new User(
                "brodyr",
                "dodie997",
                "dodiegas@gmail.com",
                "Dodie",
                "Gasmussen",
                "f",
                "0234567890ac"
        );

        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }


    @Test
    public void insertPass() throws Exception {
        db.clearTables();
        User check;

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(UserA);
            check = uDao.find(UserA.getUserName());
            assertNotNull(check);
            assertEquals(check, UserA);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void insertFail() throws Exception {
        db.clearTables();
        boolean didItWork = true;

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(UserA);
            uDao.insert(Counterfeit);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            didItWork = false;
            db.closeConnection(false);
        }

        assertFalse(didItWork);
        User checker = UserA;

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            checker = uDao.find(UserA.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(checker);
    }

    @Test
    public void findPass() throws Exception {
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(UserA);
            assertEquals(UserA, uDao.find(UserA.getUserName()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void findFail() throws Exception {
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            //Ensures that a deleted User cannot be found in the database
            uDao.insert(UserA);
            assertNotNull(uDao.find(UserA.getUserName()));
            uDao.delete(UserA.getUserName());
            assertNull(uDao.find(UserA.getUserName()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void clear() throws Exception {
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            //Ensure that multiple users can be added, and deleted with the single call, clear()
            uDao.insert(UserA);
            uDao.insert(UserB);
            uDao.clear();
            assertNull(uDao.find(UserA.getUserName()));
            assertNull(uDao.find(UserB.getUserName()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}