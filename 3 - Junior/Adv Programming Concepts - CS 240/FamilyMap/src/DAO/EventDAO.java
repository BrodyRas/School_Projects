package DAO;

import Models.Event;
import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventDAO {
    private Connection conn;

    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts event into table
     *
     * @param event Event to be inserted
     * @return true for a successful insertion, false if rollback is desired
     * @throws DataAccessException
     */
    public boolean insert(Event event) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getDescendant());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            throw new DataAccessException("Error encountered while inserting event into the database");
        }

        return commit;
    }

    /**
     * Attempts to find event by searching for its ID
     *
     * @param eventID ID of event to be found
     * @return Event whose ID matches
     * @throws DataAccessException
     */
    public Event find(String eventID) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String newEventID = rs.getString("EventID");
                String newDescendant = rs.getString("Descendant");
                String newPersonID = rs.getString("PersonID");
                float newLatitude = rs.getFloat("Latitude");
                float newLongitude = rs.getFloat("Longitude");
                String newCountry = rs.getString("Country");
                String newCity = rs.getString("City");
                String newType = rs.getString("EventType");
                int newYear = rs.getInt("Year");

                return new Event(newEventID, newDescendant, newPersonID, newCountry, newCity, newLatitude, newLongitude, newType, newYear);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        return null;
    }

    /**
     * Return all events connected to the specified user's family tree
     *
     * @param user the user whose events are to be parsed
     * @return Set of all events associated with user
     */
    public Event[] associatedEvents(User user) throws DataAccessException {
        ArrayList<Event> events = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String newEventID = rs.getString("EventID");
                String newDescendant = rs.getString("Descendant");
                String newPersonID = rs.getString("PersonID");
                float newLatitude = rs.getFloat("Latitude");
                float newLongitude = rs.getFloat("Longitude");
                String newCountry = rs.getString("Country");
                String newCity = rs.getString("City");
                String newType = rs.getString("EventType");
                int newYear = rs.getInt("Year");

                Event event = new Event(newEventID, newDescendant, newPersonID, newCountry, newCity, newLatitude, newLongitude, newType, newYear);
                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding associated events");
        }
        Event[] answer = new Event[events.size()];
        answer = events.toArray(answer);
        return answer;
    }

    /**
     * Deletes all persons associated with specified user from table
     *
     * @param userName user whose data is being erased
     * @return true for a successful deletion, false for an error
     */
    public boolean deleteAssociatedEvents(String userName) throws DataAccessException {
        String sql = "DELETE FROM Events " +
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
     * @return true if successfully cleared
     */
    public boolean clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing Events");
        }
        return true;
    }
}
