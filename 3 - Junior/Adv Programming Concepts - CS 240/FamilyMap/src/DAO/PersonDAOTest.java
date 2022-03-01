package DAO;

import Models.Person;
import Models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class PersonDAOTest {
    private Database db;
    private Person PersonA, PersonB, PersonC, Counterfeit;
    private User UserA;

    @Before
    public void setUp() throws Exception {
        db = new Database();

        PersonA = new Person(
                "1234567890ab",
                "brodyr",
                "Brody",
                "Rasmussen",
                "m");

        PersonB = new Person(
                "0987654321ba",
                "brodyr",
                "Jennifer",
                "Christiensen",
                "f");

        PersonC = new Person(
                "1234567890abc",
                "dodieg",
                "Grody",
                "Rasmussen",
                "m");

        Counterfeit = new Person(
                "1234567890ab",
                "dodieg",
                "Dodie",
                "Gasmussen",
                "f");

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
        Person compareTest = null;
        //Insert the person into our DB, use find to pull the person from the DB, ensure they're the same
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(PersonA);
            compareTest = pDao.find(PersonA.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(PersonA, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        //Because PersonID's must be unique, attempting to insert someone w an identical ID
        //will cause a rollback, meaning that nothing will be added
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(PersonA);
            pDao.insert(Counterfeit);       //the duplicate PersonID causes an error
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        //Failure to find a person returns null, so let's ensure compareTest becomes null after
        //failing find the requested person
        Person compareTest = PersonA;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            compareTest = pDao.find(PersonA.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        Person compareTest;

        //Prove the DB's ability to add and find multiple different persons
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            //Successfully find PersonA
            pDao.insert(PersonA);
            compareTest = pDao.find(PersonA.getPersonID());
            assertNotNull(compareTest);
            assertEquals(compareTest, PersonA);

            //Successfully find PersonB
            pDao.insert(PersonB);
            compareTest = pDao.find(PersonB.getPersonID());
            assertNotNull(compareTest);
            assertEquals(compareTest, PersonB);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void findFail() throws Exception {
        //Attempting to find someone who was added to, then deleted from, the DB should
        //return null, just as searching for someone who was never added
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            //Successfully added
            pDao.insert(PersonA);
            assertNotNull(pDao.find(PersonA.getPersonID()));

            //Deleted
            pDao.delete(PersonA.getPersonID());
            assertNull(pDao.find(PersonA.getPersonID()));

            //Never added
            assertNull(pDao.find(PersonB.getPersonID()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void associatedPass() throws Exception {
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            //A and B come from UserA, counterfeit does not
            pDao.insert(PersonA);
            pDao.insert(PersonB);
            pDao.insert(PersonC);

            //What result should look like
            Person[] model = {PersonA, PersonB};

            //Ensure result looks like model set
            Person[] result = pDao.associatedPersons(UserA);
            assertArrayEquals(result, model);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void associatedFail() throws Exception {
        //Confirms that searching a database w/o any associated persons returns an empty set
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(Counterfeit);
            Person[] empty = {};
            assertEquals(0, empty.length);

            Person[] result = pDao.associatedPersons(UserA);
            assertArrayEquals(result, empty);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void clear() throws Exception {
        //Ensure that multiple persons can be added, and deleted with the single call, clear()
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(PersonA);
            pDao.insert(PersonB);
            pDao.clear();

            assertNull(pDao.find(PersonA.getPersonID()));
            assertNull(pDao.find(PersonB.getPersonID()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}