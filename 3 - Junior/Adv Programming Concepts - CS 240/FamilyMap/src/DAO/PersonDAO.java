package DAO;

import Models.Person;
import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDAO {
    private Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts person into database
     *
     * @param person the person to be inserted
     * @return true for a successful insertion, false if rollback is desired
     * @throws DataAccessException
     */
    public boolean insert(Person person) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO Persons (PersonID, Descendant, FirstName, LastName, Gender, Father, Mother, Spouse) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getDescendant());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFather());
            stmt.setString(7, person.getMother());
            stmt.setString(8, person.getSpouse());
            stmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            commit = false;
            throw new DataAccessException("Error encountered while inserting person into the database");
        }
        return commit;
    }

    /**
     * Attempts to find person by searching for its ID
     *
     * @param personID ID of person to be found
     * @return person who matches the specified ID
     * @throws DataAccessException
     */
    public Person find(String personID) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String newPersonID = rs.getString("PersonID");
                String newDescendant = rs.getString("Descendant");
                String newFirstName = rs.getString("FirstName");
                String newLastName = rs.getString("LastName");
                String newGender = rs.getString("Gender");
                String newFather = rs.getString("Father");
                String newMother = rs.getString("Mother");
                String newSpouse = rs.getString("Spouse");

                return new Person(newPersonID, newDescendant, newFirstName, newLastName, newGender, newFather, newMother, newSpouse);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
        return null;
    }

    /**
     * Returns all persons connected to the specified user's family tree
     *
     * @param user the user whose events are to be parsed
     * @return Set of all persons associated with user
     */
    public Person[] associatedPersons(User user) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String newPersonID = rs.getString("PersonID");
                String newDescendant = rs.getString("Descendant");
                String newFirstName = rs.getString("FirstName");
                String newLastName = rs.getString("LastName");
                String newGender = rs.getString("Gender");
                String newFather = rs.getString("Father");
                String newMother = rs.getString("Mother");
                String newSpouse = rs.getString("Spouse");

                persons.add(new Person(newPersonID, newDescendant, newFirstName, newLastName, newGender, newFather, newMother, newSpouse));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding associated persons");
        }
        Person[] answer = new Person[persons.size()];
        answer = persons.toArray(answer);
        return answer;
    }

    /**
     * Deletes specified person from table
     *
     * @param PersonID ID of person to be deleted
     * @return true for a successful deletion, false for an error
     */
    public boolean delete(String PersonID) throws DataAccessException {
        String sql = "DELETE FROM Persons " +
                "WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, PersonID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting person #" + PersonID + " from database");
        }
        return true;
    }

    /**
     * Deletes all persons associated with specified user from table
     *
     * @param userName user whose data is being erased
     * @return true for a successful deletion, false for an error
     */
    public boolean deleteAssociatedPersons(String userName) throws DataAccessException {
        String sql = "DELETE FROM Persons " +
                "WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting " + userName + "'s persons from database");
        }
        return true;
    }

    /**
     * Clears the table of all data
     *
     * @return true for a successful clear, false for an error
     */
    public boolean clear() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing Persons");
        }
        return true;
    }
}
