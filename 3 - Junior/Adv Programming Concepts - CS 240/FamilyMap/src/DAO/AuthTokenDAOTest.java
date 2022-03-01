package DAO;

import Models.AuthToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class AuthTokenDAOTest {
    private Database db;
    private AuthToken AuthA, AuthB, Counterfeit;

    @Before
    public void setUp() throws Exception {
        db = new Database();

        AuthA = new AuthToken("brodyr", "1234567890");
        AuthB = new AuthToken("jennyc", "0987654321");
        Counterfeit = new AuthToken("dodieg", "1234567890");

        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(AuthA);
            AuthToken auth = aDao.find(AuthA.getToken());
            db.closeConnection(true);

            assertEquals(AuthA, auth);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(AuthA);
            aDao.insert(Counterfeit);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            didItWork = false;
            db.closeConnection(false);
        }

        assertFalse(didItWork);

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            assertNull(aDao.find(AuthA.getUserName()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void findPass() throws Exception {
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            //Proves individual AuthTokens can be found from database
            aDao.insert(AuthA);
            aDao.insert(AuthB);
            assertEquals(AuthA, aDao.find(AuthA.getToken()));
            assertEquals(AuthB, aDao.find(AuthB.getToken()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void findFail() throws Exception {
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            assertNull(aDao.find(AuthA.getToken()));
            aDao.insert(AuthA);
            aDao.clear();
            assertNull(aDao.find(AuthA.getToken()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void clear() throws Exception {
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(AuthA);
            aDao.insert(AuthB);
            aDao.clear();
            assertNull(aDao.find(AuthA.getUserName()));
            assertNull(aDao.find(AuthB.getUserName()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}