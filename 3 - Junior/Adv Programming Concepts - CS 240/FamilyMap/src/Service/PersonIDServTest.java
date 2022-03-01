package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Models.AuthToken;
import Models.Person;
import Results.PersonResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;

public class PersonIDServTest {
    private Database db;
    private Person PersonA, PersonB;
    private AuthToken authToken;
    private PersonResult ResultA, ResultB, ResultC;

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

        authToken = new AuthToken("brodyr", "12345");

        ResultA = new PersonResult(PersonA);
        ResultB = new PersonResult(PersonB);
        ResultC = new PersonResult("Person not found");
        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void findByIDPass() throws Exception {
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(authToken);
            pDao.insert(PersonA);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        PersonIDServ personIDServ = new PersonIDServ();
        PersonResult personResult = personIDServ.findByID(authToken.getToken(), PersonA.getPersonID());
        assertEquals(ResultA, personResult);
    }

    @Test
    public void findByIDFail() throws Exception {
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(authToken);
            pDao.insert(PersonA);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Proves the Service doesn't retrieve Persons which were never added to the Database
        PersonIDServ personIDServ = new PersonIDServ();
        PersonResult personResult = personIDServ.findByID(authToken.getToken(), PersonB.getPersonID());
        assertEquals(ResultC, personResult);

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.clear();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Proves the Service cannot retrieve cleared Persons from the Database
        personResult = personIDServ.findByID(authToken.getToken(), PersonA.getPersonID());
        assertEquals(ResultC, personResult);
    }
}