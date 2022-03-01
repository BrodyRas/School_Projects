package Service;

import DAO.*;
import Models.AuthToken;
import Models.Person;
import Models.User;
import Results.PersonResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersonServTest {
    private Database db;
    private Person PersonA, PersonB, PersonC, PersonD;
    private PersonResult ResultA, ResultB, ResultC, ResultD;
    private User UserA;
    private AuthToken authToken;

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

        PersonD = new Person(
                "qwertyuidd",
                "dodieg",
                "Dodie",
                "Gasmussen",
                "f");
        ResultA = new PersonResult(new Person[]{PersonA, PersonB});
        ResultB = new PersonResult(new Person[]{PersonB, PersonC});
        ResultD = new PersonResult("Persons not found");

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
    public void getPersonsPass() throws Exception {
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(authToken);
            uDao.insert(UserA);
            pDao.insert(PersonA);
            pDao.insert(PersonB);
            pDao.insert(PersonD);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        PersonServ personServ = new PersonServ();
        PersonResult personResult = personServ.getPersons(authToken.getToken());
        assertEquals(ResultA, personResult);
    }

    @Test
    public void getPersonsFail() throws Exception {
        //Create PersonResult with empty set
        Person[] empty = new Person[]{};
        assertTrue(empty.length == 0);
        PersonResult emptyResult = new PersonResult(empty);

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(authToken);
            uDao.insert(UserA);
            pDao.insert(PersonD);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Calling associatedPersons on a user w/o any associated persons returns an empty set
        PersonServ personServ = new PersonServ();
        PersonResult personResult = personServ.getPersons(authToken.getToken());
        assertEquals(emptyResult, personResult);

    }
}