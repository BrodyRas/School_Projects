package Service;

import DAO.*;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

public class LoadServTest {
    private Database db;
    private User UserA, UserB;
    private Person PersonA, PersonB;
    private Event EventA, EventB;


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

        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void loadPass() throws Exception {
        LoadServ loadServ = new LoadServ();
        User[] users = new User[]{UserA, UserB};
        Person[] persons = new Person[]{PersonA, PersonB};
        Event[] events = new Event[]{EventA, EventB};
        LoadRequest loadRequest = new LoadRequest(users, persons, events);
        loadServ.load(loadRequest);

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);

            for (User u : users) assertNotNull(uDao.find(u.getUserName()));
            for (Person p : persons) assertNotNull(pDao.find(p.getPersonID()));
            for (Event e : events) assertNotNull(eDao.find(e.getEventID()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void loadFail() {
    }
}