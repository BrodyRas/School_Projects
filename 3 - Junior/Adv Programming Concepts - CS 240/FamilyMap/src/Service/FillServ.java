package Service;

import DAO.*;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.FillRequest;
import Results.GeneralResult;
import Utility.JsonObjectSerializer;
import Utility.Location;
import Utility.LocationList;
import Utility.NameList;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.sql.Connection;

public class FillServ {
    private NameList maleNames;
    private NameList femaleNames;
    private NameList surNames;
    private LocationList locations;
    private int eventNum, personNum;

    public FillServ() {
        JsonObjectSerializer jos = new JsonObjectSerializer();
        try {
            maleNames = jos.decode(new File("Resources/mnames.json"), NameList.class);
            femaleNames = jos.decode(new File("Resources/fnames.json"), NameList.class);
            surNames = jos.decode(new File("Resources/snames.json"), NameList.class);
            locations = jos.decode(new File("Resources/locations.json"), LocationList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventNum = 0;
        personNum = 0;
    }

    /**
     * Generates a unique PersonID for a newly created person
     *
     * @return String containing new ID
     */
    private String createID() {
        return RandomStringUtils.random(24, "0123456789abcdef");
    }

    /**
     * Provides the given user with a filled family tree
     *
     * @param fillRequest contains user who's family tree is to be filled, and number of generations to fill
     * @return Message announcing success or failure of filling generations
     */
    public GeneralResult fillGenerations(FillRequest fillRequest) throws Exception {
        Database db = new Database();
        User user = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            user = uDao.find(fillRequest.getUsername());
            db.closeConnection(true);
            if (user == null) {
                return new GeneralResult("User not found");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }

        //Create a person (with birthday event), to add to db, then create parents for user
        int baseYear = 1995;
        Person userPerson = new Person(user.getPersonID(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getGender());
        personNum++;
        Location location = locations.random();
        Event userBirth = new Event(createID(), userPerson.getDescendant(), userPerson.getPersonID(), location.getCountry(), location.getCity(), location.getLatitude(), location.getLongitude(), "Birth", baseYear);
        eventNum++;
        createParents(userPerson, fillRequest.getGenerationCount(), baseYear);

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            pDao.insert(userPerson);
            eDao.insert(userBirth);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new GeneralResult("Non-unique ID found");
        }

        return new GeneralResult("Successfully added " + personNum + " persons and " + eventNum + " events to database");
    }

    private void createParents(Person person, int genCount, int baseYear) throws Exception {
        //Create mom and dad
        Person father = new Person(createID(), person.getDescendant(), maleNames.random(), person.getLastName(), "m");
        Person mother = new Person(createID(), person.getDescendant(), femaleNames.random(), surNames.random(), "f");

        //BIRTH
        Location location = locations.random();
        Event fatherBirth = new Event(createID(),
                person.getDescendant(),
                father.getPersonID(),
                location.getCountry(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                "Birth",
                baseYear - 22);
        location = locations.random();
        Event motherBirth = new Event(createID(),
                person.getDescendant(),
                mother.getPersonID(),
                location.getCountry(),
                location.getCity(), location.
                getLatitude(),
                location.getLongitude(),
                "Birth",
                baseYear - 20);

        //BAPTISM
        location = locations.random();
        Event fatherBaptism = new Event(createID(),
                person.getDescendant(),
                father.getPersonID(),
                location.getCountry(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                "Baptism",
                baseYear - 14);
        location = locations.random();
        Event motherBaptism = new Event(createID(),
                person.getDescendant(),
                mother.getPersonID(),
                location.getCountry(),
                location.getCity(), location.
                getLatitude(),
                location.getLongitude(),
                "Baptism",
                baseYear - 12);

        //MARRIAGE
        location = locations.random();
        Event fatherMarriage = new Event(createID(),
                person.getDescendant(),
                father.getPersonID(),
                location.getCountry(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                "Marriage",
                baseYear - 1);
        Event motherMarriage = new Event(createID(),
                person.getDescendant(),
                mother.getPersonID(),
                location.getCountry(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                "Marriage",
                baseYear - 1);

        //GRADUATION
        location = locations.random();
        Event fatherGrad = new Event(createID(),
                person.getDescendant(),
                father.getPersonID(),
                location.getCountry(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                "Graduation",
                baseYear - 1);
        location = locations.random();

        Event motherGrad = new Event(createID(),
                person.getDescendant(),
                mother.getPersonID(),
                location.getCountry(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                "Graduation",
                baseYear - 1);

        //DEATH
        location = locations.random();
        Event fatherDeath = new Event(createID(),
                person.getDescendant(),
                father.getPersonID(),
                location.getCountry(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                "Death",
                baseYear + 20);
        location = locations.random();
        Event motherDeath = new Event(createID(),
                person.getDescendant(),
                mother.getPersonID(),
                location.getCountry(),
                location.getCity(),
                location.getLatitude(),
                location.getLongitude(),
                "Death",
                baseYear + 22);

        //Set relationships for parents and children
        person.setParents(father, mother);
        father.setSpouse(mother);
        mother.setSpouse(father);

        //Increment eventNum and personNum
        personNum += 2;
        eventNum += 10;

        //If there's more people to create, recursively call function on parents
        if (genCount > 1) {
            baseYear -= 22;
            genCount--;
            createParents(father, genCount, baseYear);
            createParents(mother, genCount, baseYear);
        }
        //otherwise, add parents (and events) to database
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            pDao.insert(father);
            eDao.insert(fatherBirth);
            eDao.insert(fatherBaptism);
            eDao.insert(fatherMarriage);
            eDao.insert(fatherGrad);
            eDao.insert(fatherDeath);

            pDao.insert(mother);
            eDao.insert(motherBirth);
            eDao.insert(motherBaptism);
            eDao.insert(motherMarriage);
            eDao.insert(motherGrad);
            eDao.insert(motherDeath);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }
    }
}
